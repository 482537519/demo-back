package com.example.demoback.baseModule.system.sysPermission.repository;

import com.example.demoback.baseModule.system.sysPermission.model.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionRepository extends JpaRepository<SysPermission, String>, JpaSpecificationExecutor {

    /**
     * findByName
     * @param name
     * @return
     */
    SysPermission findByName(String name);

    /**
     * findByPid
     * @param pid
     * @return
     */
    List<SysPermission> findByPid(String pid);
}
