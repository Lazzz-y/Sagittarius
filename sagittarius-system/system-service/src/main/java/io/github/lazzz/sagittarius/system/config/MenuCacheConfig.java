package io.github.lazzz.sagittarius.system.config;


import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.support.Fastjson2KeyConvertor;
import com.alicp.jetcache.support.Kryo5ValueDecoder;
import com.alicp.jetcache.support.Kryo5ValueEncoder;
import com.alicp.jetcache.template.QuickConfig;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.system.model.vo.RouteVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * 菜单缓存配置
 *
 * @author Lazzz
 * @date 2025/10/18 17:11
**/
@Configuration
@RequiredArgsConstructor
public class MenuCacheConfig {

    private final CacheManager cacheManager;

    private Cache<String, List<RouteVO>> menuCache;

    @PostConstruct
    public void init() {
        QuickConfig menuQc = QuickConfig.newBuilder(
                        CacheConstants.MENU_AREA,
                        CacheConstants.MENU_NAME)
                .build();
        menuCache = cacheManager.getOrCreateCache(menuQc);
    }

    @Bean
    public Cache<String, List<RouteVO>> menuCache() {
        return menuCache;
    }
}

