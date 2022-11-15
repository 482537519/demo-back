package com.example.demoback.baseModule.system.sysUser.model;

import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import com.example.demoback.baseModule.system.sysRole.model.SysRole;
import com.example.demoback.common.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * 系统用户
 */
@Entity
@Getter
@Setter
@Table(name="sys_user")
public class SysUser extends AbstractModel{
//    @Id
//    @Column(name = "id")
//    private String id;

    @NotBlank
    @Column(unique = true, name = "username")
    @Length(max=20,message = "账号长度超过最大限制")
    private String username;

    private String avatar;

    @NotBlank
    @Column(name = "email")
    //@Pattern(regexp = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}",message = "邮箱格式错误")
    @Length(max=30,message = "邮箱长度超过最大限制")
    private String email;

    @NotBlank
    @Column(name = "phone")
    @Length(max=11,message = "电话长度超过最大限制")
    private String phone;

    // 名称
    @Column(name = "name")
    @Length(max=20,message = "姓名长度超过最大限制")
    private String name;


    @NotNull
    private Boolean enabled;

    private String password;

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "last_password_reset_time")
    private Date lastPasswordResetTime;

    @ManyToMany
    @JoinTable(name = "sys_users_roles", joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")})
    private Set<SysRole> roles;


    @OneToOne
    @JoinColumn(name = "dept_id")
    private SysDept dept;

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", lastPasswordResetTime=" + lastPasswordResetTime +
                '}';
    }
    public @interface Update {}
}