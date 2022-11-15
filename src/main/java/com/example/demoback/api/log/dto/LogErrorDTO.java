package com.example.demoback.api.log.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class LogErrorDTO implements Serializable {

    private String id;


    /**
     * 操作账号
     */
    private String username;
    /**
     * 操作用户
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 请求ip
     */
    private String requestIp;


    /**
     * 创建日期
     */
    private Timestamp createTime;
}