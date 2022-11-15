package com.example.demoback.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 拦截器，拦截文件流
 * @author ASUS
 */
@Configuration
public class FileWebAppConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FileHeaderCheckInterceptor()) 
                .addPathPatterns("/**"); 
    }
}