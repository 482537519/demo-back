package com.example.demoback.baseModule.system.sysDict.repository;

import com.example.demoback.baseModule.system.sysDict.model.SysDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DictRepository extends JpaRepository<SysDict, String>, JpaSpecificationExecutor {

    SysDict findByName(String name);
}