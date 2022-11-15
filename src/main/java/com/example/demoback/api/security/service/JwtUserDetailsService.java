package com.example.demoback.api.security.service;

import com.example.demoback.api.security.security.JwtUser;
import com.example.demoback.baseModule.system.sysDept.dto.DeptSmallDTO;
import com.example.demoback.baseModule.system.sysDept.service.DeptService;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.sysUser.dto.UserDTO;
import com.example.demoback.baseModule.system.sysUser.service.UserService;
import com.example.demoback.common.exception.BadRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Log4j2
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtPermissionService permissionService;

    @Autowired
    private DeptService deptService;

    @Override
    public UserDetails loadUserByUsername(String username) {

        UserDTO user = userService.findByName(username);
        if (user == null) {
            //throw new BadRequestException("账号不存在");
            throw new BadRequestException("用户名或密码错误");
        } else {
            return createJwtUser(user);
        }
    }

    public UserDetails createJwtUser(UserDTO user) {
        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                Optional.ofNullable(user.getDept()).map(DeptSmallDTO::getName).orElse(null),
                permissionService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateTime(),
                user.getLastPasswordResetTime(),
                Optional.ofNullable(user.getDept()).map(DeptSmallDTO::getId).orElse(null),
                Optional.ofNullable(user.getDept()).map(DeptSmallDTO::getDeptType).orElse(null),
                user.getRoles().stream().map(RoleSmallDTO::getCode).collect(Collectors.toList())
        );
    }
}
