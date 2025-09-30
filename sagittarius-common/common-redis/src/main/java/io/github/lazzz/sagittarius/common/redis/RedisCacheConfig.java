package io.github.lazzz.sagittarius.common.redis;


import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis缓存配置
 *
 * @author Lazzz 
 * @date 2025/09/28 21:06
**/
@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory, CacheProperties cacheProperties) {
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(factory)
                .cacheDefaults(redisCacheConfiguration(cacheProperties))
                .build();
    }

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties){
        RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig();

        // 缓存 key 使用 String
        conf = conf.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));
        // 缓存 value 使用 json
        conf = conf.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        // 从配置中获取缓存配置
        CacheProperties.Redis redisProp = cacheProperties.getRedis();

        // 缓存有效期
        if (redisProp.getTimeToLive() != null){
            conf = conf.entryTtl(redisProp.getTimeToLive());
        }
        if (!redisProp.isCacheNullValues()) {
            conf = conf.disableCachingNullValues();
        }
        if (!redisProp.isUseKeyPrefix()) {
            conf = conf.disableKeyPrefix();
        }
        //覆盖默认key双冒号
        conf = conf.computePrefixWith(name -> name + ":");

        return conf;
    }
}

