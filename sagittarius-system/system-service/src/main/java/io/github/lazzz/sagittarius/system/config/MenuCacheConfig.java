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
                // 本地缓存有效期
                .localExpire(Duration.ofHours(12))
                // 本地缓存数量限制 128个缓存
                .localLimit(128)
                .expire(Duration.ofHours(24))
                // 缓存类型 BOTH 本地 + 远程
                .cacheType(CacheType.BOTH)
                // 本地远程缓存强同步
                .syncLocal(false)
                // key转换器
                .keyConvertor(Fastjson2KeyConvertor.INSTANCE)
                // value 解码器
                .valueDecoder(Kryo5ValueDecoder.INSTANCE)
                // value 编码器
                .valueEncoder(Kryo5ValueEncoder.INSTANCE)
                // 缓存空值
                .cacheNullValue(true)
                .build();
        menuCache = cacheManager.getOrCreateCache(menuQc);
    }

    @Bean
    public Cache<String, List<RouteVO>> menuCache() {
        return menuCache;
    }
}

