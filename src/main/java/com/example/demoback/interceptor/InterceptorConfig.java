package com.example.demoback.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RefererInterceptor refererInterceptor;

    /**
     * 添加过滤器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //referer拦截
        registry.addInterceptor(refererInterceptor).addPathPatterns("/**").excludePathPatterns("/", "/login", "/logout");
    }

}


