package io.github.lazzz.sagittarius.common.cache.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis 分布式锁
 *
 * @author Lazzz
 * @date 2025/10/1 20:17
 **/
@Component
public class RedisDistributedLock {
    private final StringRedisTemplate redisTemplate;
    // 释放锁的Lua脚本（保证原子性）
    private static final String UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取锁
     *
     * @param lockKey 锁的键（如"article:readCount:100"）
     * @param lockValue 唯一值（防止误释放其他线程的锁）
     * @param expire 过期时间（避免死锁）
     * @param unit 时间单位
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String lockValue, long expire, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expire, unit));
    }

    /**
     * 释放锁
     */
    public boolean unlock(String lockKey, String lockValue) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script, Collections.singletonList(lockKey), lockValue);
        return result > 0;
    }
}
