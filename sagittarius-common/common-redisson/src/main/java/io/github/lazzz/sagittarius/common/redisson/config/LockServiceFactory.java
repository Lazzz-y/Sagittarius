package io.github.lazzz.sagittarius.common.redisson.config;


import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import io.github.lazzz.sagittarius.common.redisson.service.impl.FairLockServiceImpl;
import io.github.lazzz.sagittarius.common.redisson.service.impl.ReadWriteLockServiceImpl;
import io.github.lazzz.sagittarius.common.redisson.service.impl.RedLockServiceImpl;
import io.github.lazzz.sagittarius.common.redisson.service.impl.ReentrantLockServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * redisson 组件
 *
 * @author Lazzz
 * @date 2025/10/17 19:34
 **/
@Component
public class LockServiceFactory {

    @Bean("fairLockService")
    @Scope("prototype")
    public LockService fairLockService(RedissonClient redissonClient) {
        return new FairLockServiceImpl(redissonClient);
    }

    @Bean("reentrantLockService")
    @Scope("prototype")
    public LockService reentrantLockService(RedissonClient redissonClient) {
        return new ReentrantLockServiceImpl(redissonClient);
    }

    @Bean("readWriteLockService")
    @Scope("prototype")
    public LockService readWriteLockService(RedissonClient redissonClient) {
        return new ReadWriteLockServiceImpl(redissonClient);
    }

    @Bean("redLockService")
    @Scope("prototype")
    public LockService redLockService(RedissonClient redissonClient) {
        return new RedLockServiceImpl(redissonClient);
    }

    @Bean("concurrentLockService")
    @Scope("prototype")
    public LockService concurrentLockService(RedissonClient redissonClient) {
        return new RedLockServiceImpl(redissonClient);
    }

}
