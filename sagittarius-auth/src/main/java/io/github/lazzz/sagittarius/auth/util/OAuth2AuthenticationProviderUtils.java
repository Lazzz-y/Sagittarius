package io.github.lazzz.sagittarius.auth.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

/**
 * OAuth2认证提供工具类
 *
 * @author Lazzz 
 * @date 2025/09/20 18:51
**/
public class OAuth2AuthenticationProviderUtils {

    public OAuth2AuthenticationProviderUtils(){}

    /**
     * 获取已认证的客户端凭证，如果客户端未认证则抛出无效客户端异常
     *
     * @param authentication 当前认证信息
     * @return 已认证的OAuth2客户端认证令牌
     * @throws OAuth2AuthenticationException 如果客户端无效或未认证
     */
    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    /**
     * 使指定的OAuth2授权令牌失效
     *
     * @param authorization OAuth2授权信息
     * @param token         需要失效的令牌
     * @param <T>           令牌类型，必须是OAuth2Token的子类
     * @return 更新后的OAuth2授权信息，令牌已被标记为失效
     */
    public static <T extends OAuth2Token> OAuth2Authorization invalidate(
            OAuth2Authorization authorization, T token
    ) {
        // 创建授权构建器，并将传入的令牌标记为已失效
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization)
                .token(token, metadata -> metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));

        // 如果失效的是刷新令牌，则同时使访问令牌和授权码失效
        if (OAuth2RefreshToken.class.isAssignableFrom(token.getClass())) {
            // 使访问令牌失效
            authorizationBuilder.token(
                    authorization.getAccessToken().getToken(),
                    metadata ->
                            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));
            
            // 获取授权码并检查是否已经失效，如果没有则将其标记为失效
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                    authorization.getToken(OAuth2AuthorizationCode.class);
            if (authorizationCode != null && !authorizationCode.isInvalidated()) {
                authorizationBuilder.token(
                        authorizationCode.getToken(),
                        (metadata) ->
                                metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));
            }
        }
        // 构建并返回更新后的授权信息
        return authorizationBuilder.build();
    }

}

