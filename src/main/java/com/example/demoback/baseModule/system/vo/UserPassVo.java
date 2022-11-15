package com.example.demoback.baseModule.system.vo;

import lombok.Data;

/**
 * 修改密码的 Vo 类
 */
@Data
public class UserPassVo {

    private String oldPass;

    private String newPass;
}
