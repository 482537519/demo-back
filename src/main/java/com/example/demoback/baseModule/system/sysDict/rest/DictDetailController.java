package com.example.demoback.baseModule.system.sysDict.rest;

import com.example.demoback.api.log.aop.Log;
import com.example.demoback.baseModule.system.sysDict.dto.DictDetailQueryCriteria;
import com.example.demoback.baseModule.system.sysDict.model.SysDictDetail;
import com.example.demoback.baseModule.system.sysDict.service.DictDetailService;
import com.example.demoback.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
* 字典明细
*/
@RestController
@RequestMapping("api")
public class DictDetailController {

    @Autowired
    private DictDetailService dictDetailService;

    private static final String ENTITY_NAME = "dictDetail";

    @Log("查询字典详情")
    @GetMapping(value = "/dictDetail")
    public ResponseEntity getDictDetails(DictDetailQueryCriteria criteria,
                                         @PageableDefault(value = 10, sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        //String[] names = criteria.getDictName().split(",");
        return new ResponseEntity(dictDetailService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("条件查询字典详情")
    @GetMapping(value = "/dictDetail/map")
    public ResponseEntity getDictDetailMaps(DictDetailQueryCriteria criteria,
                                         @PageableDefault(value = 10, sort = {"sort"}, direction = Sort.Direction.ASC) Pageable pageable){
        String[] names = criteria.getDictName().split(",");
        Map map = new HashMap(names.length);
        for (String name : names) {
            criteria.setDictName(name);
            map.put(name,dictDetailService.queryAll(criteria,pageable).get("content"));
        }
        return new ResponseEntity(map,HttpStatus.OK);
    }

    @Log("新增字典详情")
    @PostMapping(value = "/dictDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SysDictDetail resources){
        if (resources.getId() != null && !"".equals(resources.getId())) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(dictDetailService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改字典详情")
    @PutMapping(value = "/dictDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ResponseEntity update(@Validated(SysDictDetail.Update.class) @RequestBody SysDictDetail resources){
        dictDetailService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除字典详情")
    @DeleteMapping(value = "/dictDetail/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_DELETE')")
    public ResponseEntity delete(@PathVariable String id){
        dictDetailService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}