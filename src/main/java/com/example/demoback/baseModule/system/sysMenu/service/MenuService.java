package com.example.demoback.baseModule.system.sysMenu.service;

import com.example.demoback.baseModule.system.sysMenu.dto.MenuDTO;
import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.vo.MenuVo;
import com.example.demoback.common.dto.CommonQueryCriteria;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "menu")
public interface MenuService {

    /**
     * queryAll
     * @param criteria
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    List<MenuDTO> queryAll(CommonQueryCriteria criteria);

    /**
     * get
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    MenuDTO findById(String id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    MenuDTO create(SysMenu resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SysMenu resources);

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
    Object getMenuTree(List<SysMenu> menus);

    /**
     * findByPid
     * @param pid
     * @return
     */
    @Cacheable(key = "'pid:'+#p0")
    List<SysMenu> findByPid(String pid);

    /**
     * build Tree
     * @param menuDTOS
     * @return
     */
    Map buildTree(List<MenuDTO> menuDTOS);

    /**
     * findByRoles
     * @param roles
     * @return
     */
    List<MenuDTO> findByRoles(List<RoleSmallDTO> roles);

    /**
     * buildMenus
     * @param byRoles
     * @return
     */
    List<MenuVo>  buildMenus(List<MenuDTO> byRoles);

    SysMenu findOne(String id);
}
