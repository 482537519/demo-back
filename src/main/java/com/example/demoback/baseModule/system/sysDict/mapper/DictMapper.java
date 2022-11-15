package com.example.demoback.baseModule.system.sysDict.mapper;

import com.example.demoback.baseModule.system.sysDict.dto.DictDTO;
import com.example.demoback.baseModule.system.sysDict.model.SysDict;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapper extends EntityMapper<DictDTO, SysDict> {

}