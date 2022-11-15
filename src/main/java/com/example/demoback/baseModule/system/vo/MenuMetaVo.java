package com.example.demoback.baseModule.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

    private String title;

    private String icon;

    //隐藏页面（下钻页面、公共返回）
    private boolean breadcrumbReturn;
}
