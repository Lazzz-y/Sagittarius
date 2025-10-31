package io.github.lazzz.sagittarius.gateway.filter;


import cn.hutool.core.util.StrUtil;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * 租户全局过滤器
 * 
 * @author Lazzz 
 * @date 2025/10/10 22:24
**/
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String tenantIdStr = request.getHeaders().getFirst(SystemConstants.TENANT_HEADER);
        Long tenantId = SystemConstants.DEFAULT_TENANT_ID;
        if (StrUtil.isNotBlank(tenantIdStr)){
            try {
                tenantId = Long.parseLong(tenantIdStr);
            } catch (NumberFormatException e){
                throw new IllegalArgumentException("Invalid tenant ID: " + tenantIdStr);
            }
        }
        // 传递给下游服务
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(SystemConstants.TENANT_HEADER, SystemConstants.DEFAULT_TENANT_ID_STR)
                .build();
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();
        return chain.filter(mutatedExchange)
                .contextWrite(Context.of("tenantId", tenantId));
    }

    @Override
    public int getOrder() {
        // 确保在其他过滤器之前执行
        return -1;
    }
}

