package io.github.lazzz.sagittarius.gateway.filter;

import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayGlobalLogFilter implements GlobalFilter, Ordered {

    private final Tracer tracer;

    private static final String BEGIN_TIME_ATTR = "GATEWAY_BEGIN_TIME";
    private static final String TRACE_ID_ATTR = "GATEWAY_TRACE_ID";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Span span = tracer.nextSpan().name("gateway-filter").start();

        // 初始化请求信息
        long beginTime = System.currentTimeMillis();
        LocalDateTime requestTime = LocalDateTime.now();

        // 提取请求元数据
        String clientIp = getClientIp(exchange);
        HttpMethod httpMethod = exchange.getRequest().getMethod();
        String requestUri = exchange.getRequest().getURI().toString();
        String path = exchange.getRequest().getURI().getPath();
        String queryParams = Optional.ofNullable(exchange.getRequest().getURI().getRawQuery()).orElse("无");
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

        // 打印请求日志（结构化展示）
        log.info("""
                        
                        ────────────────────────────────────────────────────────────
                        ├─【网关请求】[{}]
                        ├─ 基础信息: {}
                        ├─ 客户端IP: {}
                        ├─ 请求方法: {}
                        ├─ 请求URI : {}
                        ├─ 请求路径: {}
                        ├─ 查询参数: {}
                        ├─ 请求头  : {}
                        ────────────────────────────────────────────────────────────""",
                requestTime.format(DATE_FORMATTER),
                "收到客户端请求",
                clientIp,
                httpMethod,
                requestUri,
                path,
                queryParams,
                // 只展示关键请求头
                getKeyHeaders(requestHeaders)
        );

        // 处理响应并记录日志
        return chain.filter(exchange)
                .doOnSuccess(v -> logResponse(exchange, beginTime, requestTime, clientIp, httpMethod, path))
                .doOnError(e -> logError(exchange, beginTime, requestTime, clientIp, httpMethod, path, e));
    }

    /**
     * 记录正常响应日志
     */
    private void logResponse(ServerWebExchange exchange, long beginTime, LocalDateTime requestTime,
                             String clientIp, HttpMethod httpMethod, String path) {
        long costTime = System.currentTimeMillis() - beginTime;
        HttpStatus statusCode = (HttpStatus) exchange.getResponse().getStatusCode();
        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();

        log.info("""
                        
                        ────────────────────────────────────────────────────────────
                        ├─【网关响应】[{}] → 耗时: {}ms
                        ├─ 基础信息: {}
                        ├─ 客户端IP: {}
                        ├─ 请求方法: {}
                        ├─ 请求路径: {}
                        ├─ 响应状态: {}
                        ├─ 响应头  : {}
                        ├─ 处理耗时: {}ms
                        ────────────────────────────────────────────────────────────""",
                LocalDateTime.now().format(DATE_FORMATTER),
                costTime,
                "请求处理完成",
                clientIp,
                httpMethod,
                path,
                statusCode == null ? "未知" : statusCode.value() + " " + statusCode.getReasonPhrase(),
                getKeyHeaders(responseHeaders),
                costTime
        );
    }

    /**
     * 记录异常响应日志
     */
    private void logError(ServerWebExchange exchange, long beginTime, LocalDateTime requestTime,
                          String clientIp, HttpMethod httpMethod, String path, Throwable e) {
        long costTime = System.currentTimeMillis() - beginTime;
        HttpStatus statusCode = (HttpStatus) exchange.getResponse().getStatusCode();

        log.error("""
                        
                        ────────────────────────────────────────────────────────────
                        ├─【网关异常】[{}] → 耗时: {}ms
                        ├─ 基础信息: {}
                        ├─ 客户端IP: {}
                        ├─ 请求方法: {}
                        ├─ 请求路径: {}
                        ├─ 响应状态: {}
                        ├─ 处理耗时: {}ms
                        ├─ 异常信息: {}
                        ────────────────────────────────────────────────────────────""",
                LocalDateTime.now().format(DATE_FORMATTER),
                costTime,
                "请求处理异常",
                clientIp,
                httpMethod,
                path,
                statusCode == null ? "500 服务器内部错误" : statusCode.value() + " " + statusCode.getReasonPhrase(),
                costTime,
                e.getMessage(),
                e
        );
    }

    /**
     * 获取客户端真实IP（处理代理场景）
     */
    private String getClientIp(ServerWebExchange exchange) {
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        return remoteAddress != null ? remoteAddress.getAddress().getHostAddress() : "未知IP";
    }

    /**
     * 提取关键头信息（避免日志冗余）
     */
    private Map<String, String> getKeyHeaders(HttpHeaders headers) {
        Map<String, String> keyHeaders = new HashMap<>();
        // 只保留关键头信息，避免日志过大
        headers.keySet().forEach(key -> {
            if (key.equalsIgnoreCase("Content-Type") ||
//                    key.equalsIgnoreCase("Authorization") ||
                    key.equalsIgnoreCase("User-Agent") ||
                    key.equalsIgnoreCase("X-Request-ID") ||
                    key.equalsIgnoreCase(SystemConstants.TENANT_HEADER)) {
                keyHeaders.put(key, headers.getFirst(key));
            }
        });
        return keyHeaders;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
