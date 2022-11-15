package com.example.demoback.baseModule.system.sysRole.model;

import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import com.example.demoback.baseModule.system.sysPermission.model.SysPermission;
import com.example.demoback.baseModule.system.sysUser.model.SysUser;
import com.example.demoback.common.models.AbstractModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 角色
 */
@Entity
@Table(name = "sys_role")
@Getter
@Setter
public class SysRole extends AbstractModel {

    @Column(nullable = false)
    @NotBlank
    @Length(max=20,message = "名称长度超过最大限制")
    private String name;

    // 数据权限类型 全部 、 本级 、 自定义
    @Column(name = "data_scope")
    private String dataScope = "本级";

    // 数值越小，级别越大
    @Column(name = "level_")
    private Integer level = 3;

    @Column
    private String remark;

    @Column
    @NotBlank
    @Length(max=20,message = "标识长度超过最大限制")
    private String code;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<SysUser> users;

    @ManyToMany
    @JoinTable(name = "sys_roles_permissions", joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private Set<SysPermission> permissions;

    @ManyToMany
    @JoinTable(name = "sys_roles_menus", joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    private Set<SysMenu> menus;

    @ManyToMany
    @JoinTable(name = "sys_roles_depts", joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "dept_id", referencedColumnName = "id")})
    private Set<SysDept> depts;

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

}
