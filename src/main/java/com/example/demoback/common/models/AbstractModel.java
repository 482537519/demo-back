package com.example.demoback.common.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.liaochong.myexcel.core.annotation.IgnoreColumn;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true,value = {"hibernateLazyInitializer", "handler","fieldHandler"})
public abstract class AbstractModel implements Serializable {
    @Id
    @GeneratedValue(generator = "customGenerationId")
    @GenericGenerator(name = "customGenerationId", strategy = "com.example.demoback.common.util.CustomGenerationId")
    @Column(name = "id", updatable = false, nullable = false)
    @NotNull(groups = {Update.class})
    @TableId(type= IdType.ID_WORKER_STR)//IdType.ID_WORKER_STR
    @IgnoreColumn
    private String id;


    public interface Update{}
}
