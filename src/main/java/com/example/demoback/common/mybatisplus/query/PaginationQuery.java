package com.example.demoback.common.mybatisplus.query;

/**
 * 分页参数
 *
 */
public abstract class PaginationQuery {
    // ================================================
    /**
     * 当前页(从自然页 0 开始)
     */
    protected Integer page = 0;
    /**
     * 每页展示数量 默认 10
     */
    protected Integer size =10;

    // ================================================

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public void setPage(Integer page) {
        this.page = page+1;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
