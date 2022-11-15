package com.example.demoback.baseModule.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo implements Serializable {

    private String id;

    private String name;

    private String path;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MenuMetaVo meta;

    //icon
    private  String  icon;
    //路由
    private  String  onePath;


    private List<MenuVo> children;

    private String  layout;

    private Boolean hidden;

    private String  module;

    private String params;

    private String color;

}
