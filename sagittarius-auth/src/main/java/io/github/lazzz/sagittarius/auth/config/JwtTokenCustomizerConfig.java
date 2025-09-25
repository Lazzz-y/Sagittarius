package io.github.lazzz.sagittarius.auth.config;


import io.github.lazzz.sagittarius.auth.model.SysUserDetails;
import io.github.lazzz.sagittarius.common.constant.JwtClaimConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 自定义 JWT 字段
 *
 * @author Lazzz
 * @date 2025/09/20 20:46
 **/
@Configuration
public class JwtTokenCustomizerConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            // 如果是访问令牌并且认证信息为用户名密码
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) && context.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
                Optional.ofNullable(context.getPrincipal().getPrincipal())
                        .ifPresent(p -> {
                            JwtClaimsSet.Builder claims = context.getClaims();
                            SysUserDetails userDetails = (SysUserDetails) p;
                            claims.claim(JwtClaimConstants.USER_ID, userDetails.getUserId());
                            claims.claim(JwtClaimConstants.USERNAME, userDetails.getUsername());
                            var authorities = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                    .stream()
                                    .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                            claims.claim(JwtClaimConstants.AUTHORITIES, authorities);
                        });
            }
        };
    }

}

