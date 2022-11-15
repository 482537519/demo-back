package com.example.demoback.baseModule.system.sysRole.dto;

import com.example.demoback.baseModule.system.sysDept.dto.DeptDTO;
import com.example.demoback.baseModule.system.sysMenu.dto.MenuDTO;
import com.example.demoback.baseModule.system.sysPermission.dto.PermissionDTO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Data
public class RoleDTO implements Serializable {

    private String id;

    private String name;

    private String dataScope;

    private Integer level;

    private String remark;

    private String code;

    private Set<PermissionDTO> permissions;

    private Set<MenuDTO> menus;

    private Set<DeptDTO> depts;

    private Timestamp createTime;
}
