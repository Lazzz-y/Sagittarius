package io.github.lazzz.sagittarius.common.redisson.config;


import io.github.lazzz.sagittarius.common.redisson.aspect.LockAspect;
import io.github.lazzz.sagittarius.common.redisson.factory.LockFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 锁自动配置
 * 
 * @author Lazzz 
 * @date 2025/10/19 22:02
**/
@Configuration
@ConditionalOnClass(LockAspect.class)
public class LockAutoConfiguration {

    @Bean
    public LockAspect lockAspect(LockFactory lockFactory) {
        return new LockAspect(lockFactory);
    }

}

