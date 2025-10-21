package io.github.lazzz.sagittarius.common.redisson.lock;


import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import lombok.Data;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 抽象锁
 * 
 * @author Lazzz 
 * @date 2025/10/20 13:10
**/
@Data
public abstract class AbstractLock implements ILock{

    protected RLock rLock;

    protected LockInfo lockInfo;

    protected RedissonClient redissonClient;

    /**
     * 获取锁
     *
     * @param name 锁名称
     * @return {@link RLock}
     */
    protected abstract RLock getLock(String name);

    @Override
    public boolean acquire() {
        try {
            rLock = getLock(lockInfo.getName());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), lockInfo.getTimeUnit());
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if (rLock.isHeldByCurrentThread()){
            rLock.unlockAsync();
        }
    }

}

