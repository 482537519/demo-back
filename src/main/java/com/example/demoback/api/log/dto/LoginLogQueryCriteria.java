package com.example.demoback.api.log.dto;

import com.example.demoback.common.annotation.Query;
import lombok.Data;

import java.util.Set;

@Data
public class LoginLogQueryCriteria {


    /**
     * loginLog表中存在部门ID字段(dept_id)
     * sysDept:是SysLoginLog中属性名称
     * id :是SysDept中的Id
     */
    @Query(propName = "id", type = Query.Type.IN, joinName = "sysDept")
    private Set<String> deptIds;

    @Query
    private String browser;

    @Query
    private String ip;

    private String deptId;

}
