package io.github.lazzz.sagittarius.common.redisson.lock;


import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 公平锁
 * 
 * @author Lazzz 
 * @date 2025/10/20 13:18
**/
public class FairLock extends AbstractLock {

    public FairLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    protected RLock getLock(String name) {
        return redissonClient.getFairLock(name);
    }


}

