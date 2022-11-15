package com.example.demoback.api.log.repository;

import com.example.demoback.api.log.model.SysLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface LoginLogRepository extends JpaRepository<SysLoginLog, String>, JpaSpecificationExecutor {
    @Modifying
    @Query(value = "UPDATE SYS_LOGIN_LOG SET LOGOUT_TIME =?2  WHERE TOKEN_KEY = ?1", nativeQuery = true)
    int upLogoutTime(String tokenKey, Timestamp outTime);


    @Modifying
    @Query(value = "UPDATE SYS_LOGIN_LOG SET TOKEN_KEY =?2  WHERE TOKEN_KEY = ?1", nativeQuery = true)
    int upToken(String oldTokenKey, String newTokenKey);

    @Modifying
    @Query(value = "DELETE FROM SYS_LOGIN_LOG  WHERE USER_ID = ?1", nativeQuery = true)
    int deleteByUserId(String userId);

    @Modifying
    @Query(value = "DELETE FROM SYS_LOGIN_LOG  WHERE DEPT_ID = ?1", nativeQuery = true)
    int deleteByDeptId(String userId);
}
