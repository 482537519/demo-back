package com.example.demoback.api.log.model;

import com.example.demoback.common.models.AbstractModel;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.IgnoreColumn;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 日志
 */
@Entity
@Data
@Table(name = "sys_log")
@NoArgsConstructor
public class SysLog extends AbstractModel {


    /**
     * 操作账号
     */
    @IgnoreColumn
    private String username;
    /**
     * 操作用户
     */
    @ExcelColumn(index = 0,title = "操作用户")
    private String name;

    /**
     * 请求ip
     */
    @Column(name = "request_ip")
    @ExcelColumn(index = 1,title = "IP")
    private String requestIp;

    /**
     * 描述
     */
    @ExcelColumn(index = 2,title = "描述")
    private String description;

    /**
     * 方法名
     */
    @ExcelColumn(index = 3,title = "方法名")
    private String method;

    /**
     * 参数
     */
    @Column(name = "params")
    @ExcelColumn(index = 4,title = "参数")
    private String params;

    /**
     * 日志类型
     */
    @Column(name = "log_type")
    private String logType;



    /**
     * 请求耗时
     */
    @ExcelColumn(index = 5,title = "请求耗时")
    private Long time;

    /**
     * 创建日期
     */
    @CreationTimestamp
    @Column(name = "create_time")
    @ExcelColumn(index = 6,title = "创建日期")
    private Timestamp createTime;
    /**
     * 异常详细
     */
    @Column(name = "exception_detail")
    @ExcelColumn(index = 7,title = "异常详细")
    private String exceptionDetail;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
