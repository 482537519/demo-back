package com.example.demoback.api.log.dto;

import com.example.demoback.common.annotation.Query;
import lombok.Data;

/**
 * 日志查询类
 */
@Data
public class  LogQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private String logType;

    @Query(type = Query.Type.INNER_LIKE)
    private String description;

    @Query(type = Query.Type.NOT_EQUAL,propName = "description")
    private String notDescription;
}
