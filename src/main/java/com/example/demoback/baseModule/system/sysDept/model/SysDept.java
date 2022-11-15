package com.example.demoback.baseModule.system.sysDept.model;

import com.example.demoback.baseModule.system.sysRole.model.SysRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

/**
* 部门
*/
@Entity
@Data
@Table(name="sys_dept")
public class SysDept {

    @Id
    private String id;
    /**
     * 名称
     */
    @Column(name = "name",nullable = false)
    @NotBlank
    @Length(max=20,message = "名称长度超过最大限制")
    private String name;

    @NotNull
    private Boolean enabled;

    /**
     * 上级部门
     */
    @Column(name = "pid",nullable = false)
    @NotNull
    private String pid;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    /**
     * 部门类型
     */
    @Column(name = "dept_type")
    private String deptType;

    @Column(name="dept_path")
    private String deptPath;

    @Column(name="sort")
    private Integer sort;

    @JsonIgnore
    @ManyToMany(mappedBy = "depts")
    private Set<SysRole> roles;


    public @interface Update {}
}