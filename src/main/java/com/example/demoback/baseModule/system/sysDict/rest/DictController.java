package com.example.demoback.baseModule.system.sysDict.rest;

import com.example.demoback.api.log.aop.Log;
import com.example.demoback.baseModule.system.sysDict.dto.DictDTO;
import com.example.demoback.baseModule.system.sysDict.model.SysDict;
import com.example.demoback.baseModule.system.sysDict.service.DictService;
import com.example.demoback.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* 字典
*/
@RestController
@RequestMapping("api")
public class DictController {

    @Autowired
    private DictService dictService;

    private static final String ENTITY_NAME = "dict";

    @Log("查询字典数据")
    @GetMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_SELECT')")
    public ResponseEntity getDicts(DictDTO resources, Pageable pageable){
        return new ResponseEntity(dictService.queryAll(resources,pageable),HttpStatus.OK);
    }

    @Log("新增字典")
    @PostMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SysDict resources){
        if (resources.getId() != null  && !"".equals(resources.getId())) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(dictService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改字典")
    @PutMapping(value = "/dict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ResponseEntity update(@Validated(SysDict.Update.class) @RequestBody SysDict resources){
        dictService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除字典")
    @DeleteMapping(value = "/dict/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_DELETE')")
    public ResponseEntity delete(@PathVariable String id){
        dictService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}