package com.example.demoback.api.security.security;

import com.example.demoback.api.log.service.LoginLogService;
import com.example.demoback.common.util.DisLockUtil;
import com.example.demoback.common.util.EncryptUtils;
import com.example.demoback.common.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {


    @Value("${jwt.online}")
    private String onlineKey;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private DisLockUtil disLockUtil;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;


    public JwtAuthorizationTokenFilter(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, RedisTemplate redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = jwtTokenUtil.getToken(request);
        OnlineUser onlineUser = null;
        try {
            onlineUser = (OnlineUser) redisTemplate.opsForValue().get(onlineKey + authToken);
            if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // It is not compelling necessary to load the use details from the database. You could also store the information
                // in the token and read it from it. It's up to you ;)
                JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(onlineUser.getUserName());
                // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                // the database compellingly. Again it's up to you ;)
                //token refresh
                //是否到了能刷新token时间
                if (jwtTokenUtil.isRefreshTokenTime(authToken)) {
                    if (disLockUtil.tryLock(authToken, 10)) {
                        //刪除旧在线用户
                        //redisTemplate.delete(onlineKey + authToken);
                        //旧token设置10s过期时间
                        redisTemplate.expire(onlineKey + authToken, 10, TimeUnit.SECONDS);
                        //生成新的token
                        String oldToken = authToken;
                        authToken = jwtTokenUtil.refreshToken(authToken);
                        //设置redis中
                        onlineUser.setKey(EncryptUtils.desEncrypt(authToken));
                        redisTemplate.opsForValue().set(onlineKey + authToken, onlineUser);
                        redisTemplate.expire(onlineKey + authToken, expiration, TimeUnit.MILLISECONDS);
                        //异步更新登录日志中token
                        loginLogService.upToken(oldToken, authToken);
                        //response返回、前端更新token
                        response.setHeader("newToken", authToken);
                        response.addHeader("Access-Control-Expose-Headers", "newToken");
                        response.setContentType("application/json;charset=utf-8");
                        response.setCharacterEncoding("UTF-8");
                    }
                }
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        chain.doFilter(request, response);
    }
}
