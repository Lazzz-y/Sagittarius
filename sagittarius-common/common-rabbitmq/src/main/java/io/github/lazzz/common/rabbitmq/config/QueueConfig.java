package io.github.lazzz.common.rabbitmq.config;


import io.github.lazzz.common.rabbitmq.constant.MQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 用户队列配置
 * 
 * @author Lazzz 
 * @date 2025/10/28 20:04
**/
@Configuration
@RequiredArgsConstructor
public class QueueConfig {

    // 定义队列
    @Bean
    public Queue userQueue() {
        Map<String, Object> args = Map.of(
                // 死信路由键
                "x-dead-letter-exchange", "dlx.exchange",
                "x-queue-type", "classic"
        );
        return QueueBuilder
                // 持久化队列
                .durable(MQConstants.QUEUE_USER_INFO)
                // 死信交换机
                .withArguments(args)
                // 消息过期时间 60s
                .ttl(60000)
                .build();
    }

    // 文章发布队列
    @Bean
    public Queue articlePublishQueue() {
        Map<String, Object> args = Map.of(
                // 死信路由键
                "x-dead-letter-exchange", "dlx.exchange",
                "x-queue-type", "classic"
        );
        return QueueBuilder
                // 持久化队列
                .durable(MQConstants.QUEUE_ARTICLE_PUBLISH)
                // 死信交换机
                .withArguments(args)
                // 消息过期时间 60s
                .ttl(60000)
                .build();
    }
}

