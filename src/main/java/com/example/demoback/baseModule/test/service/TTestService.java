package com.example.demoback.baseModule.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demoback.baseModule.test.entity.TTest;
import com.example.demoback.baseModule.test.query.TTestQuery;


public interface TTestService extends IService<TTest> {

    Object queryAll(TTestQuery tTestQuery);

    Object create(TTest resources);

    int update(TTest resources);

    int delete(String id);

}
