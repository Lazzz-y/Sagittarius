package io.github.lazzz.sagittarius.gateway.config;


import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import io.github.lazzz.sagittarius.common.result.ResultCode;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lazzz
 * @className SentinelConfig
 * @description Sentinel 配置
 * @date 2025/09/19 16:28
 **/
@Configuration
public class SentinelConfig {

    /**
     *  初始化限流异常处理
     *
     * @author Lazzz
     * @date 2025/9/19
     */
    @PostConstruct
    private void initBlockHandler() {
        GatewayCallbackManager.setBlockHandler((ex, t) ->
                ServerResponse
                        .status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ResultCode.FLOW_LIMITING.toString()))
        );
    }

    /**
     *  初始化网关限流规则
     *
     * @author Lazzz
     * @date 2025/9/19
     */
    @PostConstruct
    private void initGatewayRule(){
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("auth-service")
                // 每秒最多 100 个请求
                .setCount(100)
                // 统计时间窗口
                .setIntervalSec(1)
        );
        // 按 IP 限流
        rules.add(new GatewayFlowRule("user-service")
                .setCount(20)
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
                )
                .setIntervalSec(10)
        );
        GatewayRuleManager.loadRules(rules);
    }

}

