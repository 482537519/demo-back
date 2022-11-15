package com.example.demoback.baseModule.system.sysDict.mapper;

import com.example.demoback.baseModule.system.sysDict.dto.DictDetailDTO;
import com.example.demoback.baseModule.system.sysDict.model.SysDictDetail;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictDetailMapper extends EntityMapper<DictDetailDTO, SysDictDetail> {

}