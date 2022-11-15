package com.example.demoback.baseModule.system.sysMenu.repository;

import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.LinkedHashSet;
import java.util.List;

public interface MenuRepository extends JpaRepository<SysMenu, String>, JpaSpecificationExecutor {

    /**
     * findByName
     * @param name
     * @return
     */
    SysMenu findByName(String name);

    /**
     * findByPid
     * @param pid
     * @return
     */
    List<SysMenu> findByPid(String pid);

    LinkedHashSet<SysMenu> findByRoles_IdOrderBySortAsc(String id);
}
