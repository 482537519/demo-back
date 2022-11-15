package com.example.demoback.baseModule.system.sysDict.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DictDetailDTO implements Serializable {

    private String id;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private String sort;
}