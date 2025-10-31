package io.github.lazzz.common.rabbitmq.constant;



/**
 * RabbitMQ 常量
 * 
 * @author Lazzz 
 * @date 2025/10/28 20:13
**/
public interface MQConstants {

    ////////////////////// 交换机 //////////////////////

    /**
     * 用户交换机
     */
    String EXCHANGE_USER = "user.exchange";

    ////////////////////// 队列 //////////////////////

    /**
     * 用户更新队列
     */
    String QUEUE_USER_INFO = "user.info.queue";

    ////////////////////// 路由键 //////////////////////

    /**
     * 用户更新路由键
     */
    String ROUTING_KEY_USER_INFO = "user.info";
}

