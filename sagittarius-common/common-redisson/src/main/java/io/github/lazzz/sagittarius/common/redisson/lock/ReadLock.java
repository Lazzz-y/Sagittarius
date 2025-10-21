package io.github.lazzz.sagittarius.common.redisson.lock;


import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 读锁
 * 
 * @author Lazzz 
 * @date 2025/10/20 13:20
**/
public class ReadLock extends AbstractLock {

    public ReadLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    protected RLock getLock(String name) {
        return redissonClient.getReadWriteLock(name).readLock();
    }

}

