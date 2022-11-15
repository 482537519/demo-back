package com.example.demoback.common.mybatisplus.annotaion;

import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.example.demoback.common.mybatisplus.enums.ColumnNamingStrategy;

import java.lang.annotation.*;

/**
 * 模糊查询(LIKE) 条件注解
 *
 */
@Documented
@CriteriaQuery
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Like {
    /**
     * 自定义的属性值
     *
     * @return
     */
    String alias() default "";

    /**
     * 匹配模式
     *
     * @return SqlLike
     * @see {@link SqlLike}
     */
    SqlLike like() default SqlLike.DEFAULT;

    /**
     * 默认下划线
     *
     * @return ColumnNamingStrategy
     */
    ColumnNamingStrategy naming() default ColumnNamingStrategy.LOWER_CASE_UNDER_LINE;
}
