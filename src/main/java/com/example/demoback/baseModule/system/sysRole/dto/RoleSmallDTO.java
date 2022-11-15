package com.example.demoback.baseModule.system.sysRole.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleSmallDTO implements Serializable {

    private String id;

    private String name;

    private Integer level;

    private  String code;

    private String dataScope;
}
