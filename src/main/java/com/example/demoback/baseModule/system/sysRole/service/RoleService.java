package com.example.demoback.baseModule.system.sysRole.service;

import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import com.example.demoback.baseModule.system.sysRole.dto.RoleDTO;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.sysRole.model.SysRole;
import com.example.demoback.common.dto.CommonQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@CacheConfig(cacheNames = "role")
public interface RoleService {

    /**
     * get
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    RoleDTO findById(String id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    RoleDTO create(SysRole resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SysRole resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(String id);

    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * findByUsers_Id
     * @param id
     * @return
     */
    @Cacheable(key = "'findByUsers_Id:' + #p0")
    List<RoleSmallDTO> findByUsers_Id(String id);

    @Cacheable(keyGenerator = "keyGenerator")
    Integer findByRoles(Set<SysRole> roles);

    /**
     * updatePermission
     * @param resources
     * @param roleDTO
     */
    @CacheEvict(allEntries = true)
    void updatePermission(SysRole resources, RoleDTO roleDTO);

    /**
     * updateMenu
     * @param resources
     * @param roleDTO
     */
    @CacheEvict(allEntries = true)
    void updateMenu(SysRole resources, RoleDTO roleDTO);

    @CacheEvict(allEntries = true)
    void untiedMenu(SysMenu menu);

    /**
     * queryAll
     * @param pageable
     * @return
     */
    Object queryAll(Pageable pageable);

    /**
     * queryAll
     * @param pageable
     * @param criteria
     * @return
     */
    Object queryAll(CommonQueryCriteria criteria, Pageable pageable);


    SysRole findByCode(String code);

    int deleteUserRoles(String user_id);

    int saveUserRoles(String user_id, Set<SysRole> roles);

}
