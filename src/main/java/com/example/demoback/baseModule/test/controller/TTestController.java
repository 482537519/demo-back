package com.example.demoback.baseModule.test.controller;

import com.example.demoback.api.log.aop.Log;
import com.example.demoback.baseModule.test.entity.TTest;
import com.example.demoback.baseModule.test.query.TTestQuery;
import com.example.demoback.baseModule.test.service.TTestService;
import com.example.demoback.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class TTestController {

    @Autowired
    private TTestService tTestService;

    @Log("查询测试")
    @GetMapping(value = "/test")
    public ResponseEntity getUsers(@Validated TTestQuery tTestQuery) {
        return new ResponseEntity(tTestService.queryAll(tTestQuery), HttpStatus.OK);
    }

    @Log("新增测试")
    @PostMapping(value = "/test")
    public ResponseEntity create(@Validated @RequestBody TTest resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new test数据 cannot already have an ID");
        }
        return new ResponseEntity(tTestService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改测试")
    @PutMapping(value = "/test")
    public ResponseEntity update(@Validated(TTest.Update.class) @RequestBody TTest resources){
        tTestService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除测试")
    @DeleteMapping(value = "/test/{id}")
    public ResponseEntity delete(@PathVariable String id){
        tTestService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
