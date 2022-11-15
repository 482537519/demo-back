package com.example.demoback.common.mybatisplus.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.example.demoback.common.mybatisplus.annotaion.Like;
import org.springframework.util.StringUtils;
import com.example.demoback.common.mybatisplus.query.AbstractQuery;

import java.lang.reflect.Field;

/**
 * {@link Like} 注解处理器
 *
 * @param <QUERY>  自定义查询 Query
 * @param <ENTITY> 查询想对应的实体类型
 */
public class LikeProcessor<QUERY extends AbstractQuery, ENTITY>
        extends CriteriaAnnotationProcessorAdaptor<Like, QUERY, QueryWrapper<ENTITY>, ENTITY> {

    @Override
    public boolean process(QueryWrapper<ENTITY> queryWrapper, Field field, QUERY query, Like criteriaAnnotation) {

        final Object value = this.columnValue(field, query);
        if (this.isNullOrEmpty(value)) {
            // 属性值为 Null OR Empty 不跳出 循环
            return true;
        }

        String columnName = criteriaAnnotation.alias();
        if (StringUtils.isEmpty(columnName)) {
            columnName = this.columnName(field, criteriaAnnotation.naming());
        }
        assert columnName != null;
        SqlLike like = criteriaAnnotation.like();
        switch (like) {
            case LEFT:
                queryWrapper.likeLeft(null != value, columnName, value);
                break;
            case RIGHT:
                queryWrapper.likeRight(null != value, columnName, value);
                break;
            case DEFAULT:
                queryWrapper.like(null != value, columnName, value);
                break;
            default:
                break;
        }

        return true;
    }
}
