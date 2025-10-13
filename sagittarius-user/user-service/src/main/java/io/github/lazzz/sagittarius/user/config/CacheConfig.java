package io.github.lazzz.sagittarius.user.config;

import io.github.lazzz.sagittarius.common.redis.cache.TenantKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 *
 * @author Lazzz
 * @date 2025/09/28 21:06
**/
@Configuration
public class CacheConfig {

    @Bean
    public TenantKeyGenerator tenantKeyGenerator() {
        TenantKeyGenerator generator = new TenantKeyGenerator();
        // 可选配置：根据业务需求调整
        // 包含类名
        generator.setIncludeClassName(true);
        return generator;
    }
}