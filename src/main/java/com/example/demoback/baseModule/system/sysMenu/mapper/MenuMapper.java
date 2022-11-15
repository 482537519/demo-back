package com.example.demoback.baseModule.system.sysMenu.mapper;

import com.example.demoback.baseModule.system.sysMenu.dto.MenuDTO;
import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends EntityMapper<MenuDTO, SysMenu> {

}
