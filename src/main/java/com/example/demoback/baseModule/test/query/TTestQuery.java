package com.example.demoback.baseModule.test.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.demoback.baseModule.test.entity.TTest;
import com.example.demoback.common.mybatisplus.annotaion.Like;
import com.example.demoback.common.mybatisplus.query.AbstractQuery;
import lombok.Data;

import java.util.Date;


@Data
public class TTestQuery extends AbstractQuery<TTest> {


    @Like(alias = "name")
    private String name;

    private Integer age;

    private Date createTime;

    private Date updateTime;

}
