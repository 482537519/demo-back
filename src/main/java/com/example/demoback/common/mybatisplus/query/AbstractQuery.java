package com.example.demoback.common.mybatisplus.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demoback.common.mybatisplus.advisor.CriteriaAnnotationProcessorAdvisor;

/**
 * 抽象查询对象
 *
 */
public abstract class AbstractQuery<T> extends PaginationQuery {

    // WRAPPER

    /**
     * 通过注解自动包装查询 Wrapper
     *
     * @return
     * @see {@link QueryWrapper}
     */
    public QueryWrapper<T> autoWrapper() {
        return CriteriaAnnotationProcessorAdvisor.advise(this, new QueryWrapper<T>());
    }

    // PAGE

    /**
     * 如果需要分页的话
     * 获取分页对象
     *
     * @return IPage
     * @see {@link IPage}
     */
    public IPage populatePage() {
        return new Page(this.page, this.size);
    }
}