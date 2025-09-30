package io.github.lazzz.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 分类服务启动类
 * 负责任务分类管理、标签功能等
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CategoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CategoryApplication.class, args);
    }

}