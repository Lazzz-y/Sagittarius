package io.github.lazzz.sagittarius.auth.oauth2.handler;


import io.github.lazzz.sagittarius.common.constant.AuthConstants;
import io.github.lazzz.sagittarius.common.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 认证成功处理器
 *
 * @author Lazzz
 * @date 2025/09/20 13:04
 **/
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * MappingJackson2HttpMessageConverter 是 Spring 框架提供的一个 HTTP 消息转换器，
     * 用于将 HTTP 请求和响应的 JSON 数据与 Java 对象之间进行转换
     */
    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();
    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter = new DefaultOAuth2AccessTokenResponseMapConverter();

    /**
     * 自定义认证成功响应数据结构
     *
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 类型检查增强安全性，确保传入的认证信息是 OAuth2 访问令牌认证信息
        if (!(authentication instanceof OAuth2AccessTokenAuthenticationToken)) {
            throw new IllegalArgumentException("Authentication is not an instance of OAuth2AccessTokenAuthenticationToken");
        }

        // 将 Authentication 强制转换为 OAuth2AccessTokenAuthenticationToken 类型
        OAuth2AccessTokenAuthenticationToken accessTokenAuthenticationToken = (OAuth2AccessTokenAuthenticationToken) authentication;

        // 获取访问令牌、刷新令牌和附加参数
        OAuth2AccessToken accessToken = accessTokenAuthenticationToken.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthenticationToken.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthenticationToken.getAdditionalParameters();

        // 构建 OAuth2AccessTokenResponse 对象，用于统一响应格式
        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse
                        // 设置访问令牌
                        .withToken(accessToken.getTokenValue())
                        // 设置令牌类型（如 Bearer）
                        .tokenType(accessToken.getTokenType());

        // 如果令牌的签发时间和过期时间都存在，则计算过期时间差（单位：秒）
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            long expiresIn = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            // 设置过期时间差
            builder.expiresIn(expiresIn);
        }

        // 如果存在刷新令牌，则设置刷新令牌
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }

        // 如果存在附加参数，则添加到响应中
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }

        // 构建最终的 OAuth2 访问令牌响应对象
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();

        // 使用转换器将 OAuth2AccessTokenResponse 转换为 Map 格式，便于后续处理
        Map<String, Object> tokenResponseParameters = accessTokenResponseParametersConverter.convert(accessTokenResponse);

        // 创建 ServletServerHttpResponse 对象，用于写入 HTTP 响应
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        // 获取客户端 ID，用于区分不同的客户端请求（如 Knife4j 测试客户端）
        String clientId = accessTokenAuthenticationToken.getRegisteredClient().getClientId();

        try {
            // 如果是 Knife4j 测试客户端，则直接返回原始令牌响应（不包装为业务数据格式）
            if (AuthConstants.KNIFE4J_CLIENT_ID.equals(clientId)) {
                // Knife4j 自动填充的 access_token 需原生返回，不能包装成业务代码数据格式
                this.accessTokenHttpResponseConverter.write(tokenResponseParameters, null, httpResponse);
            } else {
                // 其他客户端则使用统一的业务数据格式包装响应（使用 Result.success 包装）
                this.accessTokenHttpResponseConverter.write(Result.success(tokenResponseParameters), null, httpResponse);
            }
        } catch (IOException | HttpMessageNotWritableException e) {
            // 捕获 IO 异常和消息不可写异常，防止认证成功但响应失败的情况
            throw new ServletException("Failed to write access token response", e);
        }
    }

}

