package io.github.lazzz.sagittarius.gateway.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Lazzz
 * @className SecurityConfig
 * @description TODO
 * @date 2025/09/19 13:09
 **/
@EnableWebFluxSecurity
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "security")
public class SecurityConfig {

    /**
     * 黑名单请求路径列表
     */
    @Setter
    private List<String> blacklistPaths;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchange ->
                        {
                            if (CollectionUtil.isNotEmpty(blacklistPaths)) {
                                exchange.pathMatchers(Convert.toStrArray(blacklistPaths)).authenticated();
                            }
                            exchange.anyExchange().permitAll();
                        }
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }


    /**
     * 用户限流key解析器
     * 当网关收到请求时，回调用这个方法
     * 从请求头中查找 User-Id 字段
     * 如果找到了就使用 User-Id 字段作为限流key
     * 如果没有找到就使用 anonymous 作为限流key
     * 配合 Nacos 配置中的 key-resolver 实现
     *
     * @return KeyResolver
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("User-Id"))
                .defaultIfEmpty("anonymous");
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(
//                AntPathRequestMatcher.antMatcher("/actuator/**"),
//                AntPathRequestMatcher.antMatcher("/admin/monitor/**"),
//                AntPathRequestMatcher.antMatcher("/admin/monitor/assets/**"),
//                AntPathRequestMatcher.antMatcher("/admin/monitor/actuator/health"),
//                AntPathRequestMatcher.antMatcher("/admin/monitor/actuator/info"));
//    }

}
