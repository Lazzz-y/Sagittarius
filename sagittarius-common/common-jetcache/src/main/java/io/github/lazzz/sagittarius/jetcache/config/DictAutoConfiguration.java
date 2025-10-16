package io.github.lazzz.sagittarius.jetcache.config;

import io.github.lazzz.sagittarius.jetcache.aspect.DictAspect;
import io.github.lazzz.sagittarius.jetcache.service.DictCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DictAspect.class)
public class DictAutoConfiguration {
    @Bean
    public DictAspect dictAspect(DictCacheService dictCacheService) {
        return new DictAspect(dictCacheService);
    }
}