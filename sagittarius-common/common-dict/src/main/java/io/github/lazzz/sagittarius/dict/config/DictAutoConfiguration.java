package io.github.lazzz.sagittarius.dict.config;

import io.github.lazzz.sagittarius.dict.aspect.DictAspect;
import io.github.lazzz.sagittarius.dict.service.DictCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 字典缓存自动配置
 *
 * @author Lazzz
 * @date 2025/10/17 08:58
 **/
@Configuration
@ConditionalOnClass(DictAspect.class)
public class DictAutoConfiguration {
    @Bean
    public DictAspect dictAspect(DictCacheService dictCacheService) {
        return new DictAspect(dictCacheService);
    }
}