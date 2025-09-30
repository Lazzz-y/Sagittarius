package io.github.lazzz.sagittarius.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 通知服务启动类
 * 负责邮件通知、WebSocket实时通知等功能
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

}