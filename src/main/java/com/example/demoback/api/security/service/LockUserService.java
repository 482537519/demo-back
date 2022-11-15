package com.example.demoback.api.security.service;

import com.example.demoback.api.security.security.LockUser;
import com.example.demoback.common.util.PageUtil;
import com.example.demoback.common.util.StringUtils;
import com.example.demoback.common.util.TimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 锁定用户服务类
 */
@Service
public class LockUserService {

    /**
     * 锁定用户前缀
     */
    @Value("${lock.lockKey}")
    private String lock;
    /**
     * 锁定时长
     */
    @Value("${lock.lockTime}")
    private Long lockTime;

    @Value("${lock.expiration}")
    private Long expiration;

    /**
     * 允许登录失败次数
     */
    @Value("${lock.count}")
    private Integer failCount;

    private RedisTemplate redisTemplate;


    public LockUserService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置用户登录失败次数
     *
     * @param username 账号
     */
    public int setFailCount(String username, HttpServletRequest request) {
        String keys = buildLock(username, request);
        synchronized (keys) {
            LockUser lockUser = (LockUser) redisTemplate.opsForValue().get(keys);
            lockUser = Optional.ofNullable(lockUser).orElse(new LockUser());
            if ((failCount - lockUser.getFailCount()) > 0) {
                int count = lockUser.getFailCount() + 1;
                lockUser = new LockUser(count, StringUtils.getIp(request), username);
                redisTemplate.opsForValue().set(keys, lockUser, expiration, TimeUnit.SECONDS);
                if (failCount - count == 0) {
                    redisTemplate.expire(keys, lockTime, TimeUnit.SECONDS);
                }
            }
            return failCount - lockUser.getFailCount();
        }
    }

    /**
     * 够建lock及key
     *
     * @param username
     * @param request
     * @return
     */
    private String buildLock(String username, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(lock);
        sb.append("-");
        sb.append(username);
        sb.append("-");
        sb.append(StringUtils.getIp(request));
        String lock = sb.toString().intern();
        return lock;
    }

    /**
     * 是否锁定
     *
     * @param username 账号
     * @return true:lock  false no lock
     */
    public boolean isLockVerify(String username, HttpServletRequest request) {
        String keys = buildLock(username, request);
        LockUser lockUser = (LockUser) redisTemplate.opsForValue().get(keys);
        lockUser = Optional.ofNullable(lockUser).orElse(new LockUser());
        return lockUser.getFailCount() >= failCount;
    }


    /**
     * 剩余解锁时间
     *
     * @param username 账号
     * @return 时间描述
     */
    public String surLockTime(String username, HttpServletRequest request) {
        String keys = buildLock(username, request);
        Long expire = redisTemplate.getExpire(keys);
        return TimeUtils.toTimeString(expire * 1000);
    }


    /**
     * 获取数据
     *
     * @param filter
     * @param pageable
     * @return
     */
    public Page<LockUser> getAll(String filter, Pageable pageable) {
        List<LockUser> lockUsers = getAll(filter);
        return new PageImpl<LockUser>(
                PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), lockUsers),
                pageable,
                lockUsers.size());
    }

    public List<LockUser> getAll(String filter) {
        List<String> keys = new ArrayList<>(redisTemplate.keys(lock + "*"));
        Collections.reverse(keys);
        List<LockUser> lockUsers = new ArrayList<>();
        for (String key : keys) {
            LockUser lockUser = (LockUser) redisTemplate.opsForValue().get(key);
            Long expire = redisTemplate.getExpire(key);
            lockUser.setExpiration(TimeUtils.toTimeString(expire * 1000));
            if (StringUtils.isNotBlank(filter)) {
                if (lockUser.toString().contains(filter)) {
                    lockUsers.add(lockUser);
                }
            } else {
                lockUsers.add(lockUser);
            }
        }
        return lockUsers;
    }

    public void unLock(LockUser lockUser) {
        String key = lock + '-' + lockUser.getUsername() + '-' + lockUser.getIp();
        redisTemplate.delete(key);
    }
}
