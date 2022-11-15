package com.example.demoback.api.security.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/***
 *
 * 描述：在线用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {
    private  String name;

    private String userName;

    private String job;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private Date loginTime;


}
