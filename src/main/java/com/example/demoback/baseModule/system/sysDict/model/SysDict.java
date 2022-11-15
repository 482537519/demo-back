package com.example.demoback.baseModule.system.sysDict.model;

import com.example.demoback.common.models.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
* 字典
*/
@Entity
@Data
@Table(name="sys_dict")
public class SysDict extends AbstractModel {
    /**
     * 字典名称
     */
    @Column(name = "name",nullable = false,unique = true)
    @NotBlank
    @Length(max=255,message = "字典名称长度超过最大限制")
    private String name;
    /**
     * 描述
     */
    @Column(name = "remark")
    @Length(max=255,message = "字典描述长度超过最大限制")
    private String remark;

    @OneToMany(mappedBy = "dict",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<SysDictDetail> dictDetails;

}
