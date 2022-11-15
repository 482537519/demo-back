package com.example.demoback.baseModule.system.sysDict.service;

import com.example.demoback.baseModule.system.sysDict.dto.DictDTO;
import com.example.demoback.baseModule.system.sysDict.model.SysDict;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

@CacheConfig(cacheNames = "dict")
public interface DictService {

    /**
     * 查询
     * @param dict
     * @param pageable
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(DictDTO dict, Pageable pageable);

    /**
     * findById
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    DictDTO findById(String id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    DictDTO create(SysDict resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SysDict resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(String id);

    @Cacheable(key = "#p0")
    SysDict findByName(String name);
}