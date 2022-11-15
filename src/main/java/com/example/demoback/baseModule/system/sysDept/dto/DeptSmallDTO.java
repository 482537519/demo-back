package com.example.demoback.baseModule.system.sysDept.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeptSmallDTO implements Serializable {

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String deptType;
}