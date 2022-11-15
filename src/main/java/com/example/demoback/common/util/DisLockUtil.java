package com.example.demoback.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 获取锁
 */
@Component
public class DisLockUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param lockName   redis中key
     * @param expireTime 过期时间
     * @return 是否获取锁
     */
    public Boolean tryLock(String lockName, int expireTime) {
        boolean result = redisTemplate.opsForValue().setIfAbsent(lockName, System.currentTimeMillis(), expireTime, TimeUnit.SECONDS);
        return result;
    }

    /**
     * 释放锁
     */
    public void releaseLock(String lockName) {
        if (StringUtils.isNotBlank(lockName)) {
            redisTemplate.delete(lockName);
        }
    }

}
