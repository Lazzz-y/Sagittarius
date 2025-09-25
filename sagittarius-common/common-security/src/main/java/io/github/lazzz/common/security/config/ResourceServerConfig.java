package io.github.lazzz.common.security.config;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import io.github.lazzz.sagittarius.common.constant.JwtClaimConstants;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 资源服务器 Security 配置
 * @author Lazzz 
 * @date 2025/09/22 19:51
**/
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security")
public class ResourceServerConfig {

    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 白名单路径列表
     */
    @Setter
    private List<String> whitelistPaths;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        log.info("whitelist path:{}", JSONUtil.toJsonStr(whitelistPaths));
        http.authorizeHttpRequests((requests) ->
                        {
                            if (CollectionUtil.isNotEmpty(whitelistPaths)) {
                                for (String whitelistPath : whitelistPaths) {
                                    requests.requestMatchers(mvcMatcherBuilder.pattern(whitelistPath)).permitAll();
                                }
                            }
                            requests.anyRequest().authenticated();
                        }
                )
                .csrf(AbstractHttpConfigurer::disable)
        ;
        http.oauth2ResourceServer(resourceServerConfigurer ->
                resourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtAuthenticationConverter())
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
        );
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
                AntPathRequestMatcher.antMatcher("/swagger-ui/**")
        );
    }

    /**
     * 配置JWT认证转换器，用于将JWT令牌转换为认证信息
     * 该转换器负责：
     * 1. 从JWT令牌中提取权限信息
     * 2. 设置权限前缀（如ROLE_）
     * 3. 指定权限信息在JWT中的声明名称
     * 4. 将JWT认证转换为Spring Security可用的认证对象
     *
     * @return Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>>
     *         JWT认证转换器
     */
    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        // 创建 JWT 权限转换器，用于提取 JWT 中的权限信息
        var converter = new JwtGrantedAuthoritiesConverter();
        // 设置权限前缀，如ROLE_USER, ROLE_ADMIN等，与AuthorizationManager中的权限校验保持一致
        converter.setAuthorityPrefix(JwtClaimConstants.AUTHORITY_PREFIX);
        // 设置JWT中权限信息的声明名称，即从JWT的哪个字段获取权限列表
        converter.setAuthoritiesClaimName(JwtClaimConstants.AUTHORITIES);
        // 创建 JWT 认证转换器，用于将 JWT 转换为 AbstractAuthenticationToken 对象
        var authConverter = new JwtAuthenticationConverter();
        // 设置权限转换器
        authConverter.setJwtGrantedAuthoritiesConverter(converter);
        // 由于是WebFlux环境，需要使用Reactive适配器包装
        return authConverter;
    }

}

