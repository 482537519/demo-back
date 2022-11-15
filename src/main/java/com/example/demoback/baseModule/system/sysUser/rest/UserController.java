package com.example.demoback.baseModule.system.sysUser.rest;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.example.demoback.api.config.DataScope;
import com.example.demoback.api.log.aop.Log;
import com.example.demoback.api.log.service.LoginLogService;
import com.example.demoback.baseModule.system.sysDept.service.DeptService;
import com.example.demoback.baseModule.system.sysRole.dto.RoleSmallDTO;
import com.example.demoback.baseModule.system.sysRole.service.RoleService;
import com.example.demoback.baseModule.system.sysUser.dto.UserQueryCriteria;
import com.example.demoback.baseModule.system.sysUser.model.SysUser;
import com.example.demoback.baseModule.system.sysUser.service.UserService;
import com.example.demoback.baseModule.system.vo.UserPassVo;
import com.example.demoback.common.exception.BadRequestException;
import com.example.demoback.common.util.EncryptUtils;
import com.example.demoback.common.util.PageUtil;
import com.example.demoback.common.util.SecurityUtils;
import com.example.demoback.common.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class UserController {

    @Value("${rsa.private_key}")
    private String privateKey;
    @Value("${default.password}")
    private String defaultPwd;

    @Autowired
    private UserService userService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    private DeptService deptService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Log("查询用户")
    @GetMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ResponseEntity getUsers(UserQueryCriteria criteria, Pageable pageable, Boolean selectFlag) {
        Set<String> deptSet = new HashSet<>();
        Set<String> result = new HashSet<>();
        Set<String> roles = new HashSet<>();

        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        if (!ObjectUtils.isEmpty(criteria.getRoleFlag())) {
            roles.addAll(Arrays.asList(StringUtils.split(criteria.getRoleFlag(), ",")));
            criteria.setRoleIds(roles);
        }
        // 数据权限
        Set<String> deptIds = dataScope.getDeptIds();
        // 片区管理查询片区负责人账号时不限制查询部门
        if (Optional.ofNullable(selectFlag).orElse(false)) {
            deptIds = new HashSet<>();
        }

        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)) {

            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);

            // 若无交集，则代表无数据权限
            criteria.setDeptIds(result);
            if (result.size() == 0) {
                return new ResponseEntity(PageUtil.toPage(null, 0), HttpStatus.OK);
            } else return new ResponseEntity(userService.queryAll(criteria, pageable), HttpStatus.OK);
            // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity(userService.queryAll(criteria, pageable), HttpStatus.OK);
        }
    }

    @Log("新增用户")
    @PostMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SysUser resources) {
        //checkLevel(resources);
        // 默认密码
        resources.setPassword(passwordEncoder.encode(defaultPwd.trim()));
        return new ResponseEntity(userService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改用户")
    @PutMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public ResponseEntity update(@Validated(SysUser.Update.class) @RequestBody SysUser resources) {
        //checkLevel(resources);
        userService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除用户")
    @DeleteMapping(value = "/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    public ResponseEntity delete(@PathVariable String id) {
        /*Integer currentLevel = Collections.min(roleService.findByUsers_Id(SecurityUtils.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = Collections.min(roleService.findByUsers_Id(id).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));

        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }*/
        loginLogService.deleteByUserId(id);
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("重置密码")
    @PutMapping(value = "/users/reset")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_RESET')")
    public ResponseEntity reset(@NotBlank String username) {
        return new ResponseEntity(userService.updatePass(username, passwordEncoder.encode(defaultPwd.trim())), HttpStatus.OK);
    }

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/users/updatePass")
    public ResponseEntity updatePass(@RequestBody UserPassVo user) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String oldPass = new String(rsa.decrypt(user.getOldPass(), KeyType.PrivateKey));
        String newPass = new String(rsa.decrypt(user.getNewPass(), KeyType.PrivateKey));
        if (!passwordEncoder.matches(oldPass, userDetails.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, userDetails.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(userDetails.getUsername(), passwordEncoder.encode(newPass));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 修改邮箱
     *
     * @param user
     * @param user
     * @return
     */
    @Log("修改邮箱")
    @PostMapping(value = "/users/updateEmail/{code}")
    public ResponseEntity updateEmail(@PathVariable String code, @RequestBody SysUser user) {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        if (!userDetails.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword()))) {
            throw new BadRequestException("密码错误");
        }
        userService.updateEmail(userDetails.getUsername(), user.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources
     */
    private void checkLevel(SysUser resources) {
        Integer currentLevel = Collections.min(roleService.findByUsers_Id(SecurityUtils.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/users/updateAvatar")
    public ResponseEntity updateAvatar(@RequestParam MultipartFile file) {
        userService.updateAvatar(file);
        return new ResponseEntity(HttpStatus.OK);
    }
}
