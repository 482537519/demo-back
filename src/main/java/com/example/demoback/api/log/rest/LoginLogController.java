package com.example.demoback.api.log.rest;

import com.example.demoback.api.config.DataScope;
import com.example.demoback.api.log.dto.LoginLogQueryCriteria;
import com.example.demoback.api.log.service.LoginLogService;
import com.example.demoback.baseModule.system.sysDept.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DataScope dataScope;

    @GetMapping(value = "/loginLog")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getLogs(LoginLogQueryCriteria criteria, Pageable pageable) {
        Set<String> deptSet = new HashSet<>();

        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
            criteria.setDeptIds(deptSet);
        }
        return new ResponseEntity(loginLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/loginLog/export")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity loginLogExport(LoginLogQueryCriteria criteria, HttpServletResponse response) {
        criteria = paramsInfo(criteria);
        loginLogService.export(criteria,response);
        return new ResponseEntity(HttpStatus.OK);
    }

    private LoginLogQueryCriteria paramsInfo(LoginLogQueryCriteria criteria) {
        Set<String> deptSet = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
            criteria.setDeptIds(deptSet);
        }
        return criteria;
    }
}
