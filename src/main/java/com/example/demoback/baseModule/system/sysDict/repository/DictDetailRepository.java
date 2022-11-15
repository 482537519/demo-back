package com.example.demoback.baseModule.system.sysDict.repository;

import com.example.demoback.baseModule.system.sysDict.model.SysDictDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DictDetailRepository extends JpaRepository<SysDictDetail, String>, JpaSpecificationExecutor {
}