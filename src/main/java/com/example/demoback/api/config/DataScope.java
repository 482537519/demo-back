package com.example.demoback.api.config;

import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import com.example.demoback.baseModule.system.sysDept.service.DeptService;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.sysRole.service.RoleService;
import com.example.demoback.baseModule.system.sysUser.dto.UserDTO;
import com.example.demoback.baseModule.system.sysUser.service.UserService;
import com.example.demoback.common.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据权限配置
 */
@Component
public class DataScope {

    private final String[] scopeType = {"全部","本级","自定义"};

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    public Set<String> getDeptIds() {

        UserDTO user = userService.findByName(SecurityUtils.getUsername());

        // 用于存储部门id
        Set<String> deptIds = new HashSet<>();

        // 查询用户角色
        List<RoleSmallDTO> roleSet = roleService.findByUsers_Id(user.getId());

        for (RoleSmallDTO role : roleSet) {

            if (scopeType[0].equals(role.getDataScope())) {
                return new HashSet<>() ;
            }

            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                deptIds.add(user.getDept().getId());
            }

            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                Set<SysDept> depts = deptService.findByRoleIds(role.getId());
                for (SysDept dept : depts) {
                    deptIds.add(dept.getId());
                    List<SysDept> deptChildren = deptService.findByPid(dept.getId());
                    if (deptChildren != null && deptChildren.size() != 0) {
                        deptIds.addAll(getDeptChildren(deptChildren));
                    }
                }
            }
        }
        return deptIds;
    }


    public List<String> getDeptChildren(List<SysDept> deptList) {
        List<String> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept!=null && dept.getEnabled()){
                        List<SysDept> depts = deptService.findByPid(dept.getId());
                        if(deptList !=null && deptList.size()!=0){
                            list.addAll(getDeptChildren(depts));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }
}
