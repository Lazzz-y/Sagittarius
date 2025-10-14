package io.github.lazzz.sagittarius.common.cache.config;


import com.alicp.jetcache.anno.support.EncoderParser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JetCache 缓存配置
 *
 * @author Lazzz
 * @date 2025/10/14 18:47
 **/
@Configuration
@RequiredArgsConstructor
public class JetCacheConfig {

    @Bean

    public EncoderParser encoderParser() {
        // 支持json序列化
        return new JsonEncoderParser();
    }
}

