package com.example.demoback.baseModule.system.sysDept.mapper;

import com.example.demoback.baseModule.system.sysDept.dto.DeptDTO;
import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapper extends EntityMapper<DeptDTO, SysDept> {


}