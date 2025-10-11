package io.github.lazzz.sagittarius.gateway.filter;


import cn.hutool.core.util.StrUtil;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import io.github.lazzz.sagittarius.common.utils.TenantHeaderUtil;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.common.utils.condition.IfFlattener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
        if (StrUtil.isNotBlank(tenantIdStr)){
            try {
                Long tenantId = Long.parseLong(tenantIdStr);
                if (tenantId > 0) {
                    // 设置租户ID到上下文
                    TenantContext.setTenantId(tenantId);
                    // 传递给下游服务
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header(SystemConstants.TENANT_HEADER, tenantIdStr)
                            .build();
                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(mutatedRequest)
                            .build();
                    return chain.filter(mutatedExchange);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid tenant ID: " + tenantIdStr);
            }
        }

        // 设置默认租户ID(开发环境)
        TenantContext.setTenantId(1L);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 确保在其他过滤器之前执行
        return Integer.MIN_VALUE;
    }
}

