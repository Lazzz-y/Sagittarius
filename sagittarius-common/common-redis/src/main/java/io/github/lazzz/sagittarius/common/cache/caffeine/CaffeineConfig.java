package io.github.lazzz.sagittarius.common.cache.caffeine;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * caffeine 缓存配置
 * 
 * @author Lazzz 
 * @date 2025/10/14 14:59
**/
@Configuration
@EnableCaching
public class CaffeineConfig {

    @Bean
    public Cache<String, Object> caffeineTemplate() {
        return Caffeine.newBuilder()
                // 初始缓存容量
                .initialCapacity(1000)
                // 最大缓存数量
                .maximumSize(10_000)
                // 缓存写入后5分钟后过期
                .expireAfterWrite(30, TimeUnit.MINUTES)
                // 统计缓存命中率
                .recordStats()
                .build();
    }

}

