package io.github.lazzz.sagittarius.auth.config;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * Spring Security 配置
 *
 * @author Lazzz
 * @date 2025/09/21 21:39
 **/
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "security")
public class SecurityConfig {

    /**
     * 白名单路径列表
     */
    @Setter
    private List<String> whitelistPaths;

    /**
     * 应用安全配置
     * 处理除 OAuth2 端点外的其他请求
     */
    @Bean
    @Order(0)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http.authorizeHttpRequests(requests ->
                        {
                            if (CollectionUtil.isNotEmpty(whitelistPaths)) {
                                for (String path : whitelistPaths) {
                                    requests.requestMatchers(mvcMatcherBuilder.pattern(path)).permitAll();
                                }
                            }
                            requests.anyRequest().authenticated();
                        }
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /**
     * 不走过滤器链的放行配置
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/webjars/**"),
                AntPathRequestMatcher.antMatcher("/doc.html"),
                AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/swagger-ui/**"));
    }

}
