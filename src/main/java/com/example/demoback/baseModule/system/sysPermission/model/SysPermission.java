package com.example.demoback.baseModule.system.sysPermission.model;

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
 * 权限
 */
@Entity
@Getter
@Setter
@Table(name = "sys_permission")
public class SysPermission extends AbstractModel {

	@NotBlank
	@Length(max=20,message = "名称长度超过最大限制")
	private String name;

	/**
	 * 上级类目
	 */
	@NotNull
	@Column(name = "pid",nullable = false)
	private String pid;

	@NotBlank
	@Length(max=20,message = "别名长度超过最大限制")
	private String alias;

	@JsonIgnore
	@ManyToMany(mappedBy = "permissions")
	private Set<SysRole> roles;

	@CreationTimestamp
	@Column(name = "create_time")
	private Timestamp createTime;

	@Override
	public String toString() {
		return "Permission{" +
				", name='" + name + '\'' +
				", pid=" + pid +
				", alias='" + alias + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
