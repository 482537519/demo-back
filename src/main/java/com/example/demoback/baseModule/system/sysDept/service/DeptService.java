package com.example.demoback.baseModule.system.sysDept.service;

import com.example.demoback.baseModule.system.sysDept.dto.DeptDTO;
import com.example.demoback.baseModule.system.sysDept.dto.DeptQueryCriteria;
import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@CacheConfig(cacheNames = "dept")
public interface DeptService {

    /**
     * queryAll
     *
     * @param criteria
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    List<DeptDTO> queryAll(DeptQueryCriteria criteria);

    /**
     * findById
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    DeptDTO findById(String id);

    /**
     * 通过id列表查
     */
    List<DeptDTO> findByIds(Collection<String> ids);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    DeptDTO create(SysDept resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SysDept resources);

    /**
     * delete
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(String id);

    /**
     * buildTree
     *
     * @param deptDTOS
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object buildTree(List<DeptDTO> deptDTOS);

    /**
     * findByPid
     *
     * @param pid
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    List<SysDept> findByPid(String pid);

    Set<SysDept> findByRoleIds(String id);


    List<DeptDTO> findDepTreeById(List<String> deptIds);

    /**
     * findDepTreeByParentId
     */
    @Cacheable(keyGenerator = "keyGenerator")
    List<DeptDTO> findDepTreeByParentId(String pid);

    List<DeptDTO> findDeptDataByType(String type);

}
