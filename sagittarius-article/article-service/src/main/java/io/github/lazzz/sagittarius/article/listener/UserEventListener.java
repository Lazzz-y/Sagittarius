package io.github.lazzz.sagittarius.article.listener;


import io.github.lazzz.common.rabbitmq.constant.MQConstants;
import io.github.lazzz.sagittarius.common.event.UserUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 用户事件监听器
 *
 * @author Lazzz
 * @date 2025/10/25 21:44
 **/
@Slf4j
@Component
public class UserEventListener {

    @RabbitListener(queues = MQConstants.QUEUE_USER_UPDATE)
    public void handleUserUpdate(UserUpdateEvent event) {
        log.info("用户更新事件：{}", event);
    }

}

