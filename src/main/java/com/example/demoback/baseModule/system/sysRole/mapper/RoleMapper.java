package com.example.demoback.baseModule.system.sysRole.mapper;

import com.example.demoback.baseModule.system.sysDept.mapper.DeptMapper;
import com.example.demoback.baseModule.system.sysMenu.mapper.MenuMapper;
import com.example.demoback.baseModule.system.sysPermission.mapper.PermissionMapper;
import com.example.demoback.baseModule.system.sysRole.dto.RoleDTO;
import com.example.demoback.baseModule.system.sysRole.model.SysRole;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class, MenuMapper.class, DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends EntityMapper<RoleDTO, SysRole> {

}
