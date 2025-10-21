package io.github.lazzz.sagittarius.common.redisson.factory;


import io.github.lazzz.sagittarius.common.redisson.lock.*;
import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 锁工厂
 * 
 * @author Lazzz 
 * @date 2025/10/19 21:35
**/
@Slf4j
@Component
public class LockFactory {

    private final Map<LockType, Function<LockInfo, ILock>> lockHandlers;

    public LockFactory(RedissonClient redissonClient) {
        this.lockHandlers = new EnumMap<>(LockType.class);
        this.lockHandlers.put(LockType.REENTRANT, lockInfo -> {
            log.debug("创建重入锁: {}", lockInfo.getName());
            return new ReentrantLock(redissonClient, lockInfo);
        });
        this.lockHandlers.put(LockType.FAIR, lockInfo -> {
            log.debug("创建公平锁: {}", lockInfo.getName());
            return new FairLock(redissonClient, lockInfo);
        });
        this.lockHandlers.put(LockType.READ, lockInfo -> {
            log.debug("创建读锁: {}", lockInfo.getName());
            return new ReadLock(redissonClient, lockInfo);
        });
        this.lockHandlers.put(LockType.WRITE, lockInfo -> {
            log.debug("创建写锁: {}", lockInfo.getName());
            return new WriteLock(redissonClient, lockInfo);
        });
    }

    public ILock getLock(LockType lockType, LockInfo lockInfo) {
        return lockHandlers.get(lockType).apply(lockInfo);
    }

}

