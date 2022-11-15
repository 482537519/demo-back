package com.example.demoback.api.log.service;

import com.example.demoback.api.log.dto.LoginLogQueryCriteria;
import com.example.demoback.api.security.security.JwtUser;
import com.example.demoback.api.security.security.OnlineUser;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;

public interface LoginLogService {
    @Async
    void create(JwtUser jwtUser, OnlineUser onlineUser);

    @Async
    void upLogoutTime(String token);

    @Async
    void upToken(String oldToken, String newToken);

    Object queryAll(LoginLogQueryCriteria criteria, Pageable pageable);

    int deleteByUserId(String userId);

    int deleteByDeptId(String deptId);

    void export(LoginLogQueryCriteria criteria, HttpServletResponse response);

}
