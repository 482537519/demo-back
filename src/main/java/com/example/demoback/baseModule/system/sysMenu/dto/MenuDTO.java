package com.example.demoback.baseModule.system.sysMenu.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class MenuDTO implements Serializable {

    private String id;

    private String name;

    private Long sort;

    private String path;

    private String component;

    private String pid;

    private Boolean iFrame;

    private String icon;

    private List<MenuDTO> children;

    private Timestamp createTime;

    private String  layout;

    private Boolean hidden;

    private String module;

    private String params;

    private String color;
}
