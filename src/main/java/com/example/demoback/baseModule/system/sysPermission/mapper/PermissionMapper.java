package com.example.demoback.baseModule.system.sysPermission.mapper;

import com.example.demoback.baseModule.system.sysPermission.dto.PermissionDTO;
import com.example.demoback.baseModule.system.sysPermission.model.SysPermission;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper extends EntityMapper<PermissionDTO, SysPermission> {

}
