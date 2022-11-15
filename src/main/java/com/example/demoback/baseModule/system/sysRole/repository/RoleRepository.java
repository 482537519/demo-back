package com.example.demoback.baseModule.system.sysRole.repository;

import com.example.demoback.baseModule.system.sysRole.model.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<SysRole, String>, JpaSpecificationExecutor {

    /**
     * findByName
     * @param name
     * @return
     */
    SysRole findByName(String name);

    SysRole findByCode(String code);

    Set<SysRole> findByUsers_Id(String id);

    Set<SysRole> findByMenus_Id(String id);

    @Modifying
    @Query(value = "delete from sys_users_roles where user_id=?1 ",nativeQuery = true)
    int deleteUserRoles(String user_id);

    @Modifying
    @Query(value = "insert into sys_users_roles(user_id, role_id) values(?1, ?2)",nativeQuery = true)
    int saveUserRoles(String user_id, String role_id);

}
