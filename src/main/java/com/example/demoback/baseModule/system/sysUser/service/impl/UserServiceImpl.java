package com.example.demoback.baseModule.system.sysUser.service.impl;

import com.example.demoback.api.redis.service.RedisService;
import com.example.demoback.baseModule.system.sysRole.service.RoleService;
import com.example.demoback.baseModule.system.sysUser.dto.UserDTO;
import com.example.demoback.baseModule.system.sysUser.dto.UserQueryCriteria;
import com.example.demoback.baseModule.system.sysUser.mapper.UserMapper;
import com.example.demoback.baseModule.system.sysUser.model.SysUser;
import com.example.demoback.baseModule.system.sysUser.repository.UserRepository;
import com.example.demoback.baseModule.system.sysUser.service.UserService;
import com.example.demoback.common.exception.BadRequestException;
import com.example.demoback.common.exception.EntityExistException;
import com.example.demoback.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisService redisService;

    @Value("${file.framework}")
    private String frameworkPath;

    @Value("${dir.framework}")
    private String frameworkDir;


    @Override
    public Object queryAll(UserQueryCriteria criteria, Pageable pageable) {
        Page<SysUser> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }

    @Override
    public String findTypeById(String deptId) {
        return userRepository.findTypeById(deptId);
    }

    @Override
    public UserDTO findById(String id) {
        Optional<SysUser> user = userRepository.findById(id);
        ValidationUtil.isNull(user, "User", "id", id);
        return userMapper.toDto(user.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(SysUser user) {

        /**
         * 账号是否重复
         */
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new BadRequestException("该账号已被使用");
        }
        /**
         * 邮箱是重复
         */
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new BadRequestException("该邮箱已被使用");
        }
        if (userRepository.findByPhone(user.getPhone()).size() > 0) {
            throw new BadRequestException("该手机号码已被使用");
        }
        user.setId(KeyWord.getKeyWordTime());
        user.setAvatar("");
//        return userMapper.toDto(userRepository.save(user));

        //保存角色
        roleService.saveUserRoles(user.getId(), user.getRoles());

        //保存用户数据
        int res = userRepository.saveUser(user.getId(), user.getUsername(), user.getEmail(), user.getName(), user.getPhone(),
                user.getPassword(), user.getEnabled(), user.getDept().getId(), user.getAvatar(), user.getLastPasswordResetTime());
        if (res == 1) {
            Optional<SysUser> userOptional = userRepository.findById(user.getId());
            ValidationUtil.isNull(userOptional, "User", "id", user.getId());
            return userMapper.toDto(userOptional.get());
        }
        throw new BadRequestException("用户新增失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUser resources) {
        Optional<SysUser> userOptional = userRepository.findById(resources.getId());
        ValidationUtil.isNull(userOptional, "User", "id", resources.getId());

        SysUser user = userOptional.get();

        SysUser user1 = userRepository.findByUsername(user.getUsername());
        SysUser user2 = null;
        if (user.getEmail() != null) {
            user2 = userRepository.findByEmail(user.getEmail());
        }

        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(SysUser.class, "username", resources.getUsername());
        }

        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new EntityExistException(SysUser.class, "email", resources.getEmail());
        }

        // 如果用户的角色改变了，需要手动清理下缓存
        if (!resources.getRoles().equals(user.getRoles())) {
            String key = "role::loadPermissionByUser:" + user.getUsername();
            redisService.delete(key);
            key = "role::findByUsers_Id:" + user.getId();
            redisService.delete(key);

            roleService.deleteUserRoles(resources.getId());
            roleService.saveUserRoles(resources.getId(), resources.getRoles());
        }

//        user.setId(resources.getId());
//        user.setUsername(resources.getUsername());
//        user.setEmail(resources.getEmail());
//        user.setEnabled(resources.getEnabled());
//        user.setRoles(resources.getRoles());
//        user.setDept(resources.getDept());
//        user.setPhone(resources.getPhone());
//        user.setName(resources.getName());
//        userRepository.save(user);

        userRepository.update(resources.getId(), resources.getUsername(), resources.getEmail(), resources.getEnabled(),
                resources.getDept().getId(), resources.getPhone(), resources.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO findByName(String userName) {
        SysUser user = null;
        if (ValidationUtil.isEmail(userName)) {
            user = userRepository.findByEmail(userName);
        } else if (ValidationUtil.isPhone(userName)) {
            List<SysUser> sysUsers = userRepository.findByPhone(userName);
            if (sysUsers.size() > 1) {
                throw new BadRequestException("该手机号码存在异常,请用账号或邮箱登录");
            } else if(!sysUsers.isEmpty()){
                user = sysUsers.get(0);
            }
        } else {
            user = userRepository.findByUsername(userName);
        }
        if (user == null) {
            //throw new BadRequestException("账号不存在");
            throw new BadRequestException("用户名或密码错误");
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    public SysUser findByNickname(String userName) {
        return userRepository.findByName(userName);
    }

    @Override
    public SysUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePass(String username, String pass) {
        return userRepository.updatePass(username, pass, new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(MultipartFile multipartFile) {
        String avatar = "avatar";
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.indexOf(suffix) != -1) {
            File file = FileUtil.upload(multipartFile, frameworkPath + avatar + File.separator);
            String url = "/file/" + frameworkDir + "/" + avatar + "/" + file.getName();
            userRepository.updateAvatar(SecurityUtils.getUsername(), url);
        } else {
            throw new BadRequestException("不支持的格式");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userRepository.updateEmail(username, email);
    }
}
