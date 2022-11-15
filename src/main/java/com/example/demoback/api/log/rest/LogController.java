package com.example.demoback.api.log.rest;

import com.example.demoback.api.log.dto.LogQueryCriteria;
import com.example.demoback.api.log.service.LogService;
import com.example.demoback.common.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping(value = "/logs")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        return new ResponseEntity(logService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/logs/export")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity LogsExport(LogQueryCriteria criteria, HttpServletResponse response) {
        logService.export(criteria,response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/logs/user")
    public ResponseEntity getUserLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("INFO");
        criteria.setUsername(SecurityUtils.getUsername());
        return new ResponseEntity(logService.queryAllByUser(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/logs/error")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getErrorLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType("ERROR");
        return new ResponseEntity(logService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/logs/error/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getErrorLogs(@PathVariable String id){
        return new ResponseEntity(logService.findByErrDetail(id), HttpStatus.OK);
    }

    @GetMapping(value = "/logs/error/export")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity LogsErrorExport(LogQueryCriteria criteria, HttpServletResponse response) {
        criteria.setLogType("ERROR");
        logService.export(criteria,response);
        return new ResponseEntity(HttpStatus.OK);
    }
}
