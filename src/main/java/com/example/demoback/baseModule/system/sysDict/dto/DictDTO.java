package com.example.demoback.baseModule.system.sysDict.dto;

import com.example.demoback.common.annotation.Query;
import lombok.Data;

import java.io.Serializable;

@Data
public class DictDTO implements Serializable {

    private String id;

    /**
     * 字典名称
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    /**
     * 描述
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String remark;
}
