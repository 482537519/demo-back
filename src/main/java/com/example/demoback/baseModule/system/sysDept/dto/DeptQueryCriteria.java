package com.example.demoback.baseModule.system.sysDept.dto;

import com.example.demoback.common.annotation.Query;
import lombok.Data;

import java.util.Set;

@Data
public class DeptQueryCriteria {

    @Query(type = Query.Type.IN, propName="id")
    private Set<String> ids;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

    @Query
    private String pid;
}