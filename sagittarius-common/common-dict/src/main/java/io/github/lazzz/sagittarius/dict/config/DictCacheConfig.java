package io.github.lazzz.sagittarius.dict.config;


import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.template.QuickConfig;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * JetCache 缓存配置
 *
 * @author Lazzz
 * @date 2025/10/14 18:47
 **/
@Configuration
@RequiredArgsConstructor
public class DictCacheConfig {

    private final CacheManager cacheManager;

    private Cache<String, List<DictDetailDTO>> dictCache;

    @PostConstruct
    public void init() {
        QuickConfig dictQc = QuickConfig.newBuilder(
                        CacheConstants.DICT_AREA,
                        CacheConstants.DICT_NAME).build();
        dictCache = cacheManager.getOrCreateCache(dictQc);
    }

    @Bean
    public Cache<String, List<DictDetailDTO>> dictCache() {
        return dictCache;
    }

}

