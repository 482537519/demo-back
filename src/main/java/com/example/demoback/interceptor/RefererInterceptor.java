package com.example.demoback.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

/**
 * Referer拦截器  防御CSRF攻击
 */
@Component
@Slf4j
public class RefererInterceptor extends HandlerInterceptorAdapter {

    /**
     * 白名单
     */
    //private String[] refererDomain = new String[]{"127.0.0.1", "10.111.15.114"};
    @Value("#{'${refererDomain.host}'.split(',')}")
    private String[] refererDomain;

    /**
     * 是否开启referer校验
     */
    private Boolean check = true;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        if (!check) {
            return true;
        }
        String referer = req.getHeader("Referer");
        String host = req.getServerName();
        // 验证非get请求
        if (!"GET".equals(req.getMethod())) {
            if (referer == null) {
                // 状态置为404
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                log.error("===============CSRF攻击拦截===============");
                return false;
            }
            java.net.URL url = null;
            try {
                url = new java.net.URL(referer);
            } catch (MalformedURLException e) {
                // URL解析异常，也置为404
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                log.error("===============CSRF攻击拦截===============");
                return false;
            }
            log.info("request.getServerName()==="+host);
            // 首先判断请求域名和referer域名是否相同
            if (!host.equals(url.getHost())) {
                // 如果不等，判断是否在白名单中
                if (refererDomain != null) {
                    for (String s : refererDomain) {
                        if (s.equals(url.getHost())) {
                            log.info("url.getHost()==="+url.getHost());
                            return true;
                        }
                    }
                }
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                log.error("===============CSRF攻击拦截===============");
                return false;
            }
        }
        return true;
    }

}


