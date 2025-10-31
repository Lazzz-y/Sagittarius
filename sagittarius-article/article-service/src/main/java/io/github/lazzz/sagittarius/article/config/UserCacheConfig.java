package io.github.lazzz.sagittarius.article.config;


import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.template.QuickConfig;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户缓存配置
 * 
 * @author Lazzz 
 * @date 2025/10/30 19:23
**/
@Configuration
@RequiredArgsConstructor
public class UserCacheConfig {

    private final CacheManager cacheManager;

    private Cache<String, String> userCache;

     @PostConstruct
    private void initUserCache() {
         QuickConfig qc = QuickConfig.newBuilder(
                 CacheConstants.USER_AREA,
                 CacheConstants.USER_NAME
         ).build();
        userCache = cacheManager.getOrCreateCache(qc);
    }

     /**
      * 用户缓存
      */
    @Bean
    public Cache<String, String> userCache() {
        return userCache;
    }



}

