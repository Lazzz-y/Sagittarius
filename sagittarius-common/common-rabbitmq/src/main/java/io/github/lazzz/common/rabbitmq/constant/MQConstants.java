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

    ////////////////////// 文章交换机 //////////////////////

    /**
     * 文章交换机
     */
    String EXCHANGE_ARTICLE = "article.exchange";

    ////////////////////// 文章队列 //////////////////////

    /**
     * 文章发布队列
     */
    String QUEUE_ARTICLE_PUBLISH = "article.publish.queue";

    ////////////////////// 文章路由键 //////////////////////

    /**
     * 文章发布路由键
     */
    String ROUTING_KEY_ARTICLE_PUBLISH = "article.publish";
}

