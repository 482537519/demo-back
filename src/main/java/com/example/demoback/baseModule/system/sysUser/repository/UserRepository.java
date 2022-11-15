package com.example.demoback.baseModule.system.sysUser.repository;

import com.example.demoback.baseModule.system.sysUser.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface UserRepository extends JpaRepository<SysUser, String>, JpaSpecificationExecutor {

    /**
     * findByUsername
     * @param username
     * @return
     */
    SysUser findByUsername(String username);

    /**
     * findByEmail
     * @param email
     * @return
     */
    SysUser findByEmail(String email);
    /**
     * findByPhone
     * @param phone
     * @return
     */
    List<SysUser> findByPhone(String phone);
    /**
     * 修改密码
     * @param username
     * @param pass
     */
    @Modifying
    @Query(value = "update sys_user set password = ?2 , last_password_reset_time = ?3 where username = ?1",nativeQuery = true)
    int updatePass(String username, String pass, Date lastPasswordResetTime);

    /**
     * 修改头像
     * @param username
     * @param url
     */
    @Modifying
    @Query(value = "update sys_user set avatar = ?2 where username = ?1",nativeQuery = true)
    void updateAvatar(String username, String url);

    /**
     * 修改邮箱
     * @param username
     * @param email
     */
    @Modifying
    @Query(value = "update sys_user set email = ?2 where username = ?1",nativeQuery = true)
    void updateEmail(String username, String email);

    /**
     * 
     * @param name
     * @return
     */
    SysUser findByName(String name);

    @Query(value = "select DEPT_TYPE from SYS_DEPT where ID = ?1",nativeQuery = true)
    String findTypeById(String id);

    @Modifying
    @Query(value = "insert into sys_user(id, username, email, `name`, phone, password, enabled, dept_id, create_time, avatar, last_password_reset_time) " +
            "values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, now(), ?9, ?10) ",nativeQuery = true)
    int saveUser(String id, String username, String email, String name, String phone, String password, Boolean enabled, String dept_id, String avatar, Date lastPasswordResetTime);

    @Modifying
    @Query(value = "update sys_user set username=?2, email=?3, `name`=?7, phone=?6, enabled=?4, dept_id=?5 where id=?1 ",nativeQuery = true)
    int update(String id, String username, String email, Boolean enabled, String dept_id, String phone, String name);

}
