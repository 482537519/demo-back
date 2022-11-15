package com.example.demoback.baseModule.system.sysUser.dto;

import com.example.demoback.baseModule.system.sysDept.dto.DeptSmallDTO;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;


@Data
public class UserDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private String id;

    private String username;

    private String avatar;

    private  String name;

    private String email;

    private String phone;

    private Boolean enabled;

    @JsonIgnore
    private String password;

    private Timestamp createTime;

    private Date lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDTO> roles;

    private DeptSmallDTO dept;

    private String deptId;
}
