package com.example.demoback.baseModule.system.sysUser.mapper;

import com.example.demoback.baseModule.system.sysDept.mapper.DeptMapper;
import com.example.demoback.baseModule.system.sysRole.mapper.RoleMapper;
import com.example.demoback.baseModule.system.sysUser.dto.UserDTO;
import com.example.demoback.baseModule.system.sysUser.model.SysUser;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {RoleMapper.class, DeptMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<UserDTO, SysUser> {

}
