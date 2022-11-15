package com.example.demoback.baseModule.system.sysDict.model;

import com.example.demoback.common.models.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
* 字典明细
*/
@Entity
@Data
@Table(name="sys_dict_detail")
public class SysDictDetail extends AbstractModel {
    /**
     * 字典标签
     */
    @Column(name = "label",nullable = false)
    @Length(max=255,message = "字典标签长度超过最大限制")
    private String label;

    /**
     * 字典值
     */
    @Column(name = "value",nullable = false)
    @Length(max=255,message = "字典值长度超过最大限制")
    private String value;

    /**
     * 排序
     */
    @Column(name = "sort")
    private String sort = "999";

    /**
     * 字典id
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "dict_id")
    private SysDict dict;
}
