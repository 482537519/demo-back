package com.example.demoback.api.log.model;

import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import com.example.demoback.baseModule.system.sysUser.model.SysUser;
import com.example.demoback.common.models.AbstractModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.liaochong.myexcel.core.annotation.IgnoreColumn;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <p>
 * <p>登录日志
 * </p>
 */
@Entity
@Getter
@Setter
@Table(name = "sys_login_log")
public class SysLoginLog extends AbstractModel {

    /**
     * 部门ID
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "dept_id")
    @IgnoreColumn
    private SysDept sysDept;

    /**
     * 用户ID
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    @IgnoreColumn
    private SysUser sysUser;

    /**
     * tokenKey
     */
    @JsonIgnore
    @Column(name = "token_key")
    @IgnoreColumn
    private String tokenKey;

    /**
     * 用户名称
     */
    @Transient
    private String userName;

    /**
     * 部门名称
     */
    @Transient
    private String deptName;

    public String getDeptName() {
        return sysDept.getName();
    }

    /**
     * ip
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 浏览器
     */
    @Column(name = "browser")
    private String browser;

    /**
     * 登录时间
     */
    @CreationTimestamp
    @Column(name = "login_time")
    private Timestamp loginTime;

    /**
     *登出时间
     */
    @Column(name = "logout_time")
    private Timestamp logoutTime;

    public String getUserName() {
        return sysUser.getName();
    }
}
