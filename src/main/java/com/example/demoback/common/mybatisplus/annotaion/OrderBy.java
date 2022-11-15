package com.example.demoback.common.mybatisplus.annotaion;


import com.example.demoback.common.mybatisplus.enums.ColumnNamingStrategy;
import com.example.demoback.common.mybatisplus.enums.OrderByEnum;

import java.lang.annotation.*;

/**
 * 排序(ORDER BY) 条件注解
 *
 */
@Documented
@CriteriaQuery
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {
    /**
     * 自定义的属性值
     *
     * @return
     */
    String alias() default "";

    /**
     * 排序
     * 说明: 由于解析器在解析注解的
     *
     * @return
     */
    OrderByEnum orderBy() default OrderByEnum.ASC;

    /**
     * 默认下划线
     *
     * @return ColumnNamingStrategy
     */
    ColumnNamingStrategy naming() default ColumnNamingStrategy.LOWER_CASE_UNDER_LINE;
}
