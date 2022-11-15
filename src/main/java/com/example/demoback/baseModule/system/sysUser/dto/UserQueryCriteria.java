package com.example.demoback.baseModule.system.sysUser.dto;

import com.example.demoback.common.annotation.Query;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;


@Data
public class UserQueryCriteria implements Serializable {

    @Query
    private String id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<String> deptIds;

    @Query(propName = "id", type = Query.Type.IN, joinName = "roles")
    private Set<String> roleIds;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    @Query(type = Query.Type.INNER_LIKE)
    private String email;

    @Query
    private Boolean enabled;

    private String deptId;

    private String roleFlag;
}
