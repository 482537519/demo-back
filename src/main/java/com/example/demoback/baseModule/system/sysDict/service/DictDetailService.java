package com.example.demoback.baseModule.system.sysDict.service;

import com.example.demoback.baseModule.system.sysDict.dto.DictDetailDTO;
import com.example.demoback.baseModule.system.sysDict.dto.DictDetailQueryCriteria;
import com.example.demoback.baseModule.system.sysDict.model.SysDictDetail;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@CacheConfig(cacheNames = "dictDetail")
public interface DictDetailService {

    /**
     * findById
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    DictDetailDTO findById(String id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    DictDetailDTO create(SysDictDetail resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SysDictDetail resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(String id);

    @Cacheable(keyGenerator = "keyGenerator")
    Map queryAll(DictDetailQueryCriteria criteria, Pageable pageable);
}