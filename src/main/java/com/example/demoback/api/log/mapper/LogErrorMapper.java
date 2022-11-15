package com.example.demoback.api.log.mapper;

import com.example.demoback.api.log.dto.LogErrorDTO;
import com.example.demoback.api.log.model.SysLog;
import com.example.demoback.common.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapper extends EntityMapper<LogErrorDTO, SysLog> {

}