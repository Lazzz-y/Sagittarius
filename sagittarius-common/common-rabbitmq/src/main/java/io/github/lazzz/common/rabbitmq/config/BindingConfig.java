package io.github.lazzz.common.rabbitmq.config;


import io.github.lazzz.common.rabbitmq.constant.MQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 * 
 * @author Lazzz 
 * @date 2025/10/28 20:04
**/
@Configuration
@RequiredArgsConstructor
public class BindingConfig {

    private final QueueConfig queueConfig;

    private final ExChangeConfig exchangeConfig;

    // 绑定交换机与队列
    @Bean
    public Binding userQueueBinding() {
        return BindingBuilder.bind(queueConfig.userQueue())
                .to(exchangeConfig.userExchange())
                .with(MQConstants.ROUTING_KEY_USER_INFO);
    }

}

