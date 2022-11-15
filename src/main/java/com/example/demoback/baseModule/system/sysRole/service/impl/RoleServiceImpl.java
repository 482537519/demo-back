package com.example.demoback.baseModule.system.sysRole.service.impl;

import com.example.demoback.baseModule.system.sysMenu.model.SysMenu;
import com.example.demoback.baseModule.system.sysRole.dto.RoleDTO;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.sysRole.mapper.RoleMapper;
import com.example.demoback.baseModule.system.sysRole.mapper.RoleSmallMapper;
import com.example.demoback.baseModule.system.sysRole.model.SysRole;
import com.example.demoback.baseModule.system.sysRole.repository.RoleRepository;
import com.example.demoback.baseModule.system.sysRole.service.RoleService;
import com.example.demoback.common.dto.CommonQueryCriteria;
import com.example.demoback.common.exception.EntityExistException;
import com.example.demoback.common.util.PageUtil;
import com.example.demoback.common.util.QueryHelp;
import com.example.demoback.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleSmallMapper roleSmallMapper;

    @Override
    public Object queryAll(Pageable pageable) {
        return roleMapper.toDto(roleRepository.findAll(pageable).getContent());
    }

    @Override
    public Object queryAll(CommonQueryCriteria criteria, Pageable pageable) {
        Page<SysRole> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    public SysRole findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public RoleDTO findById(String id) {
        Optional<SysRole> role = roleRepository.findById(id);
        ValidationUtil.isNull(role,"Role","id",id);
        return roleMapper.toDto(role.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO create(SysRole resources) {
        if(roleRepository.findByName(resources.getName()) != null){
            throw new EntityExistException(SysRole.class,"username",resources.getName());
        }
        if(roleRepository.findByCode(resources.getCode()) != null){
            throw new EntityExistException(SysRole.class,"code",resources.getCode());
        }
        return roleMapper.toDto(roleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRole resources) {

        Optional<SysRole> optionalRole = roleRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalRole,"Role","id",resources.getId());

        SysRole role = optionalRole.get();

        SysRole role1 = roleRepository.findByName(resources.getName());
        SysRole role2 = roleRepository.findByCode(resources.getCode());
        if(role1 != null && !role1.getId().equals(role.getId())){
            throw new EntityExistException(SysRole.class,"username",resources.getName());
        }
        if(role2 != null && !role2.getId().equals(role.getId())){
            throw new EntityExistException(SysRole.class,"code",resources.getCode());
        }
        role.setName(resources.getName());
        role.setCode(resources.getCode());
        role.setRemark(resources.getRemark());
        role.setDataScope(resources.getDataScope());
        role.setLevel(resources.getLevel());
        roleRepository.save(role);
    }

    @Override
    public void updatePermission(SysRole resources, RoleDTO roleDTO) {
        SysRole role = roleMapper.toEntity(roleDTO);
        role.setPermissions(resources.getPermissions());
        roleRepository.save(role);
    }

    @Override
    public void updateMenu(SysRole resources, RoleDTO roleDTO) {
        SysRole role = roleMapper.toEntity(roleDTO);
        role.setMenus(resources.getMenus());
        roleRepository.save(role);
    }

    @Override
    public void untiedMenu(SysMenu menu) {
        Set<SysRole> roles = roleRepository.findByMenus_Id(menu.getId());
        for (SysRole role : roles) {
            menu.getRoles().remove(role);
            role.getMenus().remove(menu);
            roleRepository.save(role);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<RoleSmallDTO> findByUsers_Id(String id) {
        Set<SysRole> sysRoles = roleRepository.findByUsers_Id(id);
        return roleSmallMapper.toDto(new ArrayList<>(sysRoles));
    }

    @Override
    public Integer findByRoles(Set<SysRole> roles) {
        Set<RoleDTO> roleDTOS = new HashSet<>();
        for (SysRole role : roles) {
            roleDTOS.add(findById(role.getId()));
        }
        return Collections.min(roleDTOS.stream().map(RoleDTO::getLevel).collect(Collectors.toList()));
    }

    @Override
    public int deleteUserRoles(String user_id) {
        return roleRepository.deleteUserRoles(user_id);
    }

    @Override
    public int saveUserRoles(String user_id, Set<SysRole> roles) {
        List<String> role_ids = roles.stream().map(SysRole::getId).collect(Collectors.toList());
        int res = 0;
        for (String role_id : role_ids) {
            res += roleRepository.saveUserRoles(user_id, role_id);
        }
        return res;
    }
}
