package io.github.lazzz.sagittarius.gateway.filter;


import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import com.nimbusds.jose.JWSObject;
import io.github.lazzz.sagittarius.common.constant.JwtClaimConstants;
import io.github.lazzz.sagittarius.common.constant.RedisConstants;
import io.github.lazzz.sagittarius.common.result.ResultCode;
import io.github.lazzz.sagittarius.common.utils.TenantHeaderUtil;
import io.github.lazzz.sagittarius.gateway.util.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * Token 验证全局过滤器
 *
 * @author Lazzz
 * @date 2025/09/25 18:56
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidationGlobalFilter implements GlobalFilter, Ordered {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求和响应对象
        var request = exchange.getRequest();
        var response = exchange.getResponse();

        // 获取请求头中的授权信息
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 如果请求头中没有授权信息，则直接返回
        if (StrUtil.isBlank(authorization) || !StrUtil.startWithIgnoreCase(authorization, JwtClaimConstants.BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        try {
            // 获取令牌
            String token = authorization.substring(JwtClaimConstants.BEARER_PREFIX.length());
            // 解析令牌
            JWSObject jwsObject = JWSObject.parse(token);
            // 获取令牌ID
            String jti = (String) jwsObject.getPayload().toJSONObject().get(JWTPayload.JWT_ID);
            // 检查令牌是否在黑名单中
            Boolean isBlackToken = redisTemplate.hasKey(RedisConstants.TOKEN_BLACKLIST_PREFIX + jti);
            // 如果在黑名单中，则返回错误信息
            if (isBlackToken) {
                return WebFluxUtils.writeErrorResponse(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
            }
        } catch (ParseException e) {
            log.error("Parsing token failed in TokenValidationGlobalFilter", e);
            return WebFluxUtils.writeErrorResponse(response, ResultCode.TOKEN_INVALID);
        }
        // 继续处理请求
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -999;
    }
}

