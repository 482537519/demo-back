package com.example.demoback.api.log.service;

import com.example.demoback.api.log.dto.LogQueryCriteria;
import com.example.demoback.api.log.model.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;

public interface LogService {

    /**
     * queryAll
     * @param criteria
     * @param pageable
     * @return
     */
    Object queryAll(LogQueryCriteria criteria, Pageable pageable);

    /**
     * queryAllByUser
     * @param criteria
     * @param pageable
     * @return
     */
    Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable);

    /**
     * 新增日志
     * @param username
     * @param name
     * @param ip
     * @param joinPoint
     * @param log
     */
    @Async
    void save(String username,String name, String ip, ProceedingJoinPoint joinPoint, SysLog log);

    /**
     * 查询异常详情
     * @param id
     * @return
     */
    Object findByErrDetail(String id);
    /**
     * 导出日志
     * @param criteria
     * @param response
     */
    void export(LogQueryCriteria criteria, HttpServletResponse response);
}
