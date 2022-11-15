package com.example.demoback.baseModule.system.sysMenu.service.impl;

import com.example.demoback.baseModule.system.sysMenu.dto.MenuDTO;
import com.example.demoback.baseModule.system.sysMenu.mapper.MenuMapper;
import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import com.example.demoback.baseModule.system.sysMenu.repository.MenuRepository;
import com.example.demoback.baseModule.system.sysMenu.service.MenuService;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.vo.MenuMetaVo;
import com.example.demoback.baseModule.system.vo.MenuVo;
import com.example.demoback.common.dto.CommonQueryCriteria;
import com.example.demoback.common.exception.BadRequestException;
import com.example.demoback.common.util.QueryHelp;
import com.example.demoback.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List queryAll(CommonQueryCriteria criteria) {
        return menuMapper.toDto(menuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public MenuDTO findById(String id) {
        Optional<SysMenu> menu = menuRepository.findById(id);
        ValidationUtil.isNull(menu, "Menu", "id", id);
        return menuMapper.toDto(menu.get());
    }

    @Override
    public List<MenuDTO> findByRoles(List<RoleSmallDTO> roles) {
        Set<SysMenu> menus = new LinkedHashSet<>();
        for (RoleSmallDTO role : roles) {
            List<SysMenu> menus1 = new ArrayList<>(menuRepository.findByRoles_IdOrderBySortAsc(role.getId()));
            menus.addAll(menus1);
        }
        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MenuDTO create(SysMenu resources) {
        if ("0".equals(resources.getPid())) {
            resources.setLayout(resources.getLayout());
        } else {
            resources.setLayout(null);
        }
        if (resources.getIFrame()) {
            if (!(resources.getPath().toLowerCase().startsWith("http://") || resources.getPath().toLowerCase().startsWith("https://"))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        return menuMapper.toDto(menuRepository.save(resources));
    }

    @Override
    public void update(SysMenu resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Optional<SysMenu> optionalPermission = menuRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalPermission, "Permission", "id", resources.getId());

        if (resources.getIFrame()) {
            if (!(resources.getPath().toLowerCase().startsWith("http://") || resources.getPath().toLowerCase().startsWith("https://"))) {
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        SysMenu menu = optionalPermission.get();
        menu.setName(resources.getName());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setSort(resources.getSort());
        menu.setParams(resources.getParams());
        menu.setColor(resources.getColor());
        if ("0".equals(resources.getPid())) {
            menu.setLayout(resources.getLayout());
        } else {
            menu.setLayout(null);
        }
        menu.setHidden(resources.getHidden());
        menu.setModule(resources.getModule());
        menuRepository.save(menu);
    }

    @Override
    public void delete(String id) {
        menuRepository.deleteById(id);
    }

    @Override
    public Object getMenuTree(List<SysMenu> menus) {
        menus.sort(Comparator.comparingInt(m->m.getSort().intValue()));
        List<Map<String, Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
                    if (menu != null) {
                        List<SysMenu> menuList = menuRepository.findByPid(menu.getId());
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", menu.getId());
                        map.put("label", menu.getName());
                        if (menuList != null && menuList.size() != 0) {
                            map.put("children", getMenuTree(menuList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }


    @Override
    public List<SysMenu> findByPid(String pid) {
        return menuRepository.findByPid(pid);
    }

    @Override
    public Map buildTree(List<MenuDTO> menuDTOS) {
        List<MenuDTO> trees = new ArrayList<MenuDTO>();
        menuDTOS.sort(Comparator.comparingInt(m->m.getSort().intValue()));

        for (MenuDTO menuDTO : menuDTOS) {

            if ("0".equals(menuDTO.getPid().toString())) {
                trees.add(menuDTO);
            }

            for (MenuDTO it : menuDTOS) {
                if (it.getPid().equals(menuDTO.getId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<MenuDTO>());
                    }
                    menuDTO.getChildren().add(it);
                }
            }
        }
        Map map = new HashMap();
        map.put("content", trees.size() == 0 ? menuDTOS : trees);
        map.put("totalElements", menuDTOS.size());
        return map;
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuDTO> menuDTOS) {
        List<MenuVo> list = new LinkedList<>();
        menuDTOS.forEach(menuDTO -> {
            MenuVo menuVo = new MenuVo();
            menuVo.setId(menuDTO.getId());
            menuVo.setName(menuDTO.getPath());
            menuVo.setPath(menuDTO.getPath());
            menuVo.setComponent(menuDTO.getComponent());
            menuVo.setMeta(new MenuMetaVo(menuDTO.getName(), menuDTO.getIcon(),menuDTO.getHidden()));
            menuVo.setIcon(menuDTO.getIcon());
            menuVo.setOnePath(menuDTO.getPath());
            menuVo.setParams(menuDTO.getParams());
            menuVo.setColor(menuDTO.getColor());
            List<MenuDTO> menuDTOList = menuDTO.getChildren();
            if (menuDTOList != null && menuDTOList.size() > 0) {
                menuVo.setChildren(buildMenus(menuDTOList));
            }
            if ("system".equals(menuDTO.getLayout()) && "0".equals(menuDTO.getPid())) {
                menuVo.setComponent("leftNav");
                menuVo.setRedirect("noredirect");
                if (menuDTOList != null && menuDTOList.size() > 0) {
                    menuVo.setOnePath(menuDTO.getPath() + "/" + menuDTOList.get(0).getPath());
                }
            } else if ("system".equals(menuDTO.getLayout()) && !"0".equals(menuDTO.getPid())) {
                menuVo.setComponent("main");
                menuVo.setRedirect("noredirect");
            } else if (menuDTO.getChildren() != null) {
                menuVo.setComponent("main");
                menuVo.setRedirect("noredirect");
            }
            menuVo.setLayout(menuDTO.getLayout());
            menuVo.setHidden(menuDTO.getHidden());
            menuVo.setModule(menuDTO.getModule());
            list.add(menuVo);
        });
        return list;
    }

    @Override
    public SysMenu findOne(String id) {
        Optional<SysMenu> menu = menuRepository.findById(id);
        ValidationUtil.isNull(menu, "Menu", "id", id);
        return menu.get();
    }
}
