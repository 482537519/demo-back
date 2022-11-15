package com.example.demoback.api.security.security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LockUser {
    /**
     * 失败次数峰值
     */
    private int failCount;

    /**
     * ip
     */
    private String ip;
    /**
     * 账号
     */
    private String username;
    /**
     * 锁定时长
     */
    private String expiration;

    public LockUser(int failCount, String ip, String username) {
        this.failCount = failCount;
        this.ip = ip;
        this.username = username;
    }
}
