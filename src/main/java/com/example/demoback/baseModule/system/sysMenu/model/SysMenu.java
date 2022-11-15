package com.example.demoback.baseModule.system.sysMenu.model;

import com.example.demoback.baseModule.system.sysRole.model.SysRole;
import com.example.demoback.common.models.AbstractModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 菜单
 */
@Entity
@Getter
@Setter
@Table(name = "sys_menu")
public class SysMenu extends AbstractModel {
    @NotBlank
    @Length(max=20,message = "名称长度超过最大限制")
    private String name;

    @Column(unique = true)
    @NotNull
    private Long sort;

    @Column(name = "path")
    @Length(max=100,message = "链接地址长度超过最大限制")
    private String path;

    @Length(max=60,message = "菜单路径超过最大限制")
    private String component;

    private String icon;

    /**
     * 上级菜单ID
     */
    @Column(name = "pid",nullable = false)
    private String pid;

    /**
     * 是否为外链 true/false
     */
    @Column(name = "i_frame")
    private Boolean iFrame;

    @ManyToMany(mappedBy = "menus")
    @JsonIgnore
    private Set<SysRole> roles;

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 系统布局:system（上左右）
     * 系统模块:layout  （上下）
     */
    @Column(name = "layout")
    private String  layout;

    @Column(name = "hidden")
    private Boolean hidden;

    /**
     * 1:顶部菜单
     * 2.工作台模块
     */
    @Column(name = "module")
    private String  module;

    @Column(name = "params")
    @Length(max=100,message = "参数长度超过最大限制")
    private String params;

    @Column(name = "color")
    private String color;



    public interface Update{}

}
