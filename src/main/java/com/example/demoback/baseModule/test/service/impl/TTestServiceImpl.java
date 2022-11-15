package com.example.demoback.baseModule.test.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demoback.baseModule.test.entity.TTest;
import com.example.demoback.baseModule.test.mapper.TTestMapper;
import com.example.demoback.baseModule.test.query.TTestQuery;
import com.example.demoback.baseModule.test.service.TTestService;
import com.example.demoback.common.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class TTestServiceImpl extends ServiceImpl<TTestMapper, TTest> implements TTestService {
    @Autowired
    private TTestMapper tTestMapper;

    @Override
    public Object queryAll(TTestQuery tTestQuery) {
        IPage<TTest> listFlowPage = tTestMapper.selectPage(tTestQuery.populatePage(), tTestQuery.autoWrapper());
        return PageUtil.toPage(listFlowPage.getRecords(), listFlowPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object create(TTest resources) {
        resources.setCreateTime(new Date());
        return tTestMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TTest resources) {
        resources.setUpdateTime(new Date());
        return tTestMapper.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String id) {
        //return tTestMapper.deleteById(id);
        return tTestMapper.deleteDataById(id);
    }
}
