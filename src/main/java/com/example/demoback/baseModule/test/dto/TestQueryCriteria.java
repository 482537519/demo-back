package com.example.demoback.baseModule.test.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.demoback.common.annotation.Query;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;


@Data
public class TestQueryCriteria implements Serializable {

    @Query
    private String name;

    private Integer age;
}
