package io.github.lazzz.sagittarius.common.redisson.lock;


import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 写锁
 * 
 * @author Lazzz 
 * @date 2025/10/20 13:21
**/
public class WriteLock extends AbstractLock {

    public WriteLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    protected RLock getLock(String name) {
        return redissonClient.getReadWriteLock(name).writeLock();
    }

}

