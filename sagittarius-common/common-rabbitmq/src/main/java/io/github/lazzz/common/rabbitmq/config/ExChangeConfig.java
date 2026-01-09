package io.github.lazzz.common.rabbitmq.config;


import io.github.lazzz.common.rabbitmq.constant.MQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户交换机配置
 * 
 * @author Lazzz 
 * @date 2025/10/28 19:41
**/
@Configuration
@RequiredArgsConstructor
public class ExChangeConfig {

    // 定义交换机
    @Bean
    public TopicExchange userExchange() {
        return ExchangeBuilder.topicExchange(MQConstants.EXCHANGE_USER)
                .durable(true)
                .build();
    }

    // 文章交换机
    @Bean
    public TopicExchange articleExchange() {
        return ExchangeBuilder.topicExchange(MQConstants.EXCHANGE_ARTICLE)
                .durable(true)
                .build();
    }

}

