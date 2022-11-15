package com.example.demoback.baseModule.system.sysUser.service;

import com.example.demoback.baseModule.system.sysUser.dto.UserDTO;
import com.example.demoback.baseModule.system.sysUser.dto.UserQueryCriteria;
import com.example.demoback.baseModule.system.sysUser.model.SysUser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@CacheConfig(cacheNames = "user")
public interface UserService {

    /**
     * get
     * @param id
     * @return
     */
    UserDTO findById(String id);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    UserDTO create(SysUser resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(SysUser resources);

    /**
     * delete
     * @param id
     */
    @CacheEvict(allEntries = true)
    void delete(String id);

    /**
     * findByName
     * @param userName
     * @return
     */
    UserDTO findByName(String userName);

    SysUser findByNickname(String userName);

    SysUser findByEmail(String email);


    /**
     * 修改密码
     * @param username
     * @param encryptPassword
     */
    @CacheEvict(allEntries = true)
    int updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     * @param file
     */
    @CacheEvict(allEntries = true)
    void updateAvatar(MultipartFile file);

    /**
     * 修改邮箱
     * @param username
     * @param email
     */
    @CacheEvict(allEntries = true)
    void updateEmail(String username, String email);

    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(UserQueryCriteria criteria, Pageable pageable);

    String findTypeById(String deptId);
}
