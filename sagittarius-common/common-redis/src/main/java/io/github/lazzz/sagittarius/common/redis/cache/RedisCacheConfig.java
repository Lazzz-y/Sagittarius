package io.github.lazzz.sagittarius.common.redis.cache;


import io.github.lazzz.sagittarius.common.utils.TenantContext;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
        conf = conf.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer() {
                    @Override
                    public String deserialize(byte[] bytes) {
                        return super.deserialize(bytes);
                    }
                    @Override
                    public byte[] serialize(String key) {
                        // 获取租户Id 拼接到最前面
                        Long tenantId = getEffectiveTenantId();
                        // 租户ID:cacheNames::具体Key
                        String newKey = tenantId + ":" + key;
                        return super.serialize(newKey);
                    }
                }
        ));
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

    /**
     * 获取有效租户ID（处理空值场景）
     */
    private Long getEffectiveTenantId() {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            return tenantId;
        }
        throw new IllegalStateException("租户上下文未获取到租户ID，且未配置默认租户ID");
    }
}

