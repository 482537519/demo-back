package com.example.demoback.baseModule.system.sysPermission.service;

import com.example.demoback.baseModule.system.sysPermission.dto.PermissionDTO;
import com.example.demoback.baseModule.system.sysPermission.model.SysPermission;
import com.example.demoback.common.dto.CommonQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = "permission")
public interface PermissionService {

    /**
     * get
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    PermissionDTO findById(String id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    PermissionDTO create(SysPermission resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SysPermission resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(String id);

    /**
     * permission tree
     * @return
     */
    @Cacheable(key = "'tree'")
    Object getPermissionTree(List<SysPermission> permissions);

    /**
     * findByPid
     * @param pid
     * @return
     */
    @Cacheable(key = "'pid:'+#p0")
    List<SysPermission> findByPid(String pid);

    /**
     * build Tree
     * @param permissionDTOS
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object buildTree(List<PermissionDTO> permissionDTOS);

    /**
     * queryAll
     * @param criteria
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    List<PermissionDTO> queryAll(CommonQueryCriteria criteria);
}
