package com.example.demoback.api.security.rest;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.example.demoback.api.log.service.LoginLogService;
import com.example.demoback.api.redis.service.RedisService;
import com.example.demoback.api.security.security.*;
import com.example.demoback.api.security.service.LockUserService;
import com.example.demoback.api.security.service.OnlineUserService;
import com.example.demoback.baseModule.system.sysUser.dto.UserDTO;
import com.example.demoback.baseModule.system.sysUser.service.UserService;
import com.example.demoback.common.exception.BadRequestException;
import com.example.demoback.common.util.JwtTokenUtil;
import com.example.demoback.common.util.SecurityUtils;
import com.example.demoback.common.util.StringUtils;
import com.example.demoback.common.util.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zjq
 * @create 2022-09-30 13:32
 */
@RestController
@RequestMapping("/auth")
public class AuthorLoginController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private LockUserService lockUserService;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private UserService userService;


    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${rsa.private_key}")
    private String privateKey;
    @Value("${single.login}")
    private Boolean singleLogin;
    @Value("${single.vCode}")
    private Boolean vCode;
    @Value("${default.password}")
    private String defaultPassword;

    /**
     * 登录授权
     */
    @PostMapping(value = "${jwt.auth.path}")
    public ResponseEntity login(@Validated @RequestBody AuthorizationUser authorizationUser, HttpServletRequest request) {
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(authorizationUser.getPassword(), KeyType.PrivateKey));
        //账号解密
        authorizationUser.setUsername(new String(rsa.decrypt(authorizationUser.getUsername(), KeyType.PrivateKey)));
        if (lockUserService.isLockVerify(authorizationUser.getUsername(), request)) {
            throw new BadRequestException("IP账号被锁定请" + lockUserService.surLockTime(authorizationUser.getUsername(), request) + "再试!");
        }
        // 查询验证码
        String code = redisService.getCodeVal(authorizationUser.getUuid());
        // 清除验证码
        redisService.delete(authorizationUser.getUuid());
        if (vCode) {
            if (StringUtils.isBlank(code)) {
                throw new BadRequestException("验证码不存在或已过期");
            }
            if (StringUtils.isBlank(authorizationUser.getCode()) || !authorizationUser.getCode().equalsIgnoreCase(code)) {
                throw new BadRequestException("验证码错误");
            }
        }

        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authorizationUser.getUsername());
        if (!passwordEncoder.matches(password, jwtUser.getPassword())) {
            int subCount = lockUserService.setFailCount(jwtUser.getUsername(), request);
            throw new AccountExpiredException("用户名或密码错误,还有" + subCount + "次机会");
        }
        if (!jwtUser.isEnabled()) {
            throw new AccountExpiredException("账号已停用，请联系管理员");
        }

        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);
        // 保存在线信息
        OnlineUser onlineUser = onlineUserService.save(jwtUser, token, request);
        if (singleLogin) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authorizationUser.getUsername(), token);
        }
        //异步处理-登录日志
        loginLogService.create(jwtUser, onlineUser);

        // 返回 token
        return ResponseEntity.ok(new AuthenticationInfo(token, jwtUser));
    }

    /**
     * 获取用户信息
     */
    @GetMapping(value = "${jwt.auth.account}")
    public ResponseEntity getUserInfo() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

    /**
     * 获取验证码
     * 限流注解，该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_code，
     */
    @GetMapping(value = "vCode")
    public ImgResult getCode(HttpServletResponse response) throws IOException {
        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        String uuid = IdUtil.simpleUUID();
        redisService.saveCode(uuid, verifyCode);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try {
            return new ImgResult(Base64.encode(stream.toByteArray()), uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            stream.close();
        }
    }

    /**
     * 退出登录
     */
    @DeleteMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String token = jwtTokenUtil.getToken(request);
        onlineUserService.logout(token);
        loginLogService.upLogoutTime(token);
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 验证是否为默认密码
     *
     * @return
     */
    @GetMapping(value = "/checkDefaultPassword")
    public ResponseEntity checkDefaultPassword(String id) {
        Map<String,Object> result =  new HashMap<>();
        result.put("success",Boolean.FALSE);
        UserDTO user = userService.findById(id);
        if(null != user){
            if(StringUtils.isNotBlank(defaultPassword)){
                if (passwordEncoder.matches(defaultPassword, user.getPassword())) {
                    result.put("success",Boolean.TRUE);
                }
            }
        }
        return ResponseEntity.ok(result);
    }
}
