package com.example.demoback.baseModule.system.sysDept.rest;

import com.example.demoback.api.config.DataScope;
import com.example.demoback.api.log.aop.Log;
import com.example.demoback.api.log.service.LoginLogService;
import com.example.demoback.baseModule.system.sysDept.dto.DeptDTO;
import com.example.demoback.baseModule.system.sysDept.dto.DeptQueryCriteria;
import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import com.example.demoback.baseModule.system.sysDept.service.DeptService;
import com.example.demoback.common.exception.BadRequestException;
import com.example.demoback.common.util.SecurityUtils;
import com.example.demoback.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private DataScope dataScope;

    private static final String ENTITY_NAME = "dept";

    @Log("查询所有部门数据")
    @GetMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT','DEPT_ALL','DEPT_SELECT')")
    public ResponseEntity getDepts(DeptQueryCriteria criteria) {
        // 数据权限
        criteria.setIds(dataScope.getDeptIds());
        List<DeptDTO> deptDTOS = deptService.queryAll(criteria);
        return new ResponseEntity(deptService.buildTree(deptDTOS), HttpStatus.OK);
    }

    @Log("新增部门")
    @PostMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SysDept resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity(deptService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改部门数据")
    @PutMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_EDIT')")
    public ResponseEntity update(@Validated(SysDept.Update.class) @RequestBody SysDept resources) {
        deptService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除部门")
    @DeleteMapping(value = "/dept/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_DELETE')")
    public ResponseEntity delete(@PathVariable String id) {
        loginLogService.deleteByDeptId(id);
        deptService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("根据部门id查询部门树")
    @GetMapping(value = "/dept/findDepTree")
    public ResponseEntity findDepTreeById(String deptId) {
        if (StringUtils.isEmpty(deptId)){
            deptId = SecurityUtils.getDeptId();
        }
        if (!StringUtils.isEmpty(deptId)) {
            List<DeptDTO> deptDTOS = deptService.findDepTreeById(Arrays.asList(StringUtils.split(deptId, ",")));
            return new ResponseEntity(deptService.buildTree(deptDTOS), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

   @Log("根据父部门id查新部门树")
    @GetMapping(value = "/dept/findDepTreeByParentId")
    public ResponseEntity findDepTreeByParentId(String deptId) {
        if (StringUtils.isEmpty(deptId)){
            deptId = SecurityUtils.getDeptId();
        }
        if (!StringUtils.isEmpty(deptId)) {
            List<DeptDTO> deptDTOS = deptService.findDepTreeByParentId(deptId);
            return new ResponseEntity(deptService.buildTree(deptDTOS), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("通过部门类型查询部门列表")
    @GetMapping(value = "/dept/findDeptListType")
    public ResponseEntity findDepListByType(String type) {
        List<DeptDTO> deptDTOS = new ArrayList<>();

        deptDTOS = deptService.findDeptDataByType(type);
        return new ResponseEntity(deptDTOS, HttpStatus.OK);
    }
}