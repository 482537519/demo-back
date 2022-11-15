package com.example.demoback.baseModule.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demoback.baseModule.test.entity.TTest;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface TTestMapper extends BaseMapper<TTest> {

    int deleteDataById(String id);

}
