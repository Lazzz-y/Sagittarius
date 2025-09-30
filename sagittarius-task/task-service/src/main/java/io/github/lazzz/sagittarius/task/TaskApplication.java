package io.github.lazzz.sagittarius.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 任务服务启动类
 * 负责任务的CRUD、状态管理等核心业务功能
 * @author Lazzz
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

}