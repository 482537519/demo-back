package com.example.demoback.baseModule.system.sysMenu.rest;

import com.example.demoback.api.log.aop.Log;
import com.example.demoback.baseModule.system.sysMenu.dto.MenuDTO;
import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import com.example.demoback.baseModule.system.sysMenu.service.MenuService;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.sysRole.service.RoleService;
import com.example.demoback.baseModule.system.sysUser.dto.UserDTO;
import com.example.demoback.baseModule.system.sysUser.service.UserService;
import com.example.demoback.baseModule.system.vo.MenuVo;
import com.example.demoback.baseModule.system.vo.RootMenu;
import com.example.demoback.common.dto.CommonQueryCriteria;
import com.example.demoback.common.exception.BadRequestException;
import com.example.demoback.common.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final String ENTITY_NAME = "menu";

    /**
     * 构建前端路由所需要的菜单
     * @return
     */
    @GetMapping(value = "/menus/build")
    public ResponseEntity buildMenus(){
        UserDTO user = userService.findByName(SecurityUtils.getUsername());
        List<RoleSmallDTO> roleSmallDTOS = roleService.findByUsers_Id(user.getId());
        List<MenuDTO> menuDTOList = menuService.findByRoles(roleSmallDTOS);
        List<MenuDTO> menuDTOTree = (List<MenuDTO>)menuService.buildTree(menuDTOList).get("content");
        List<MenuVo>  menuVoList=menuService.buildMenus(menuDTOTree);
        RootMenu rootMenu=new RootMenu();
        rootMenu.setChildren(menuVoList);
        return new ResponseEntity(Arrays.asList(rootMenu),HttpStatus.OK);
    }

    /**
     * 返回全部的菜单
     * @return
     */
    @GetMapping(value = "/menus/tree")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_CREATE','MENU_EDIT','ROLES_SELECT','ROLES_ALL')")
    public ResponseEntity getMenuTree(){
        return new ResponseEntity(menuService.getMenuTree(menuService.findByPid("0")),HttpStatus.OK);
    }


    @Log("查询菜单")
    @GetMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT')")
    public ResponseEntity getMenus(CommonQueryCriteria criteria){
        List<MenuDTO> menuDTOList = menuService.queryAll(criteria);
        return new ResponseEntity(menuService.buildTree(menuDTOList),HttpStatus.OK);
    }

    @Log("新增菜单")
    @PostMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SysMenu resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(menuService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改菜单")
    @PutMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public ResponseEntity update(@Validated(SysMenu.Update.class) @RequestBody SysMenu resources){
        menuService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除菜单")
    @DeleteMapping(value = "/menus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_DELETE')")
    public ResponseEntity delete(@PathVariable String id){
        List<SysMenu> menuList = menuService.findByPid(id);

        // 特殊情况，对级联删除进行处理
        for (SysMenu menu : menuList) {
            roleService.untiedMenu(menu);
            menuService.delete(menu.getId());
        }
        roleService.untiedMenu(menuService.findOne(id));
        menuService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
