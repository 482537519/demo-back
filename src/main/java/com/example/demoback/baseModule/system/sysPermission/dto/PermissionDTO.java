package com.example.demoback.baseModule.system.sysPermission.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class PermissionDTO implements Serializable{

	private String id;

	private String name;

	private String pid;

	private String alias;

	private Timestamp createTime;

	private List<PermissionDTO>  children;

	@Override
	public String toString() {
		return "Permission{" +
				"id=" + id +
				", name='" + name + '\'' +
				", pid=" + pid +
				", alias='" + alias + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
