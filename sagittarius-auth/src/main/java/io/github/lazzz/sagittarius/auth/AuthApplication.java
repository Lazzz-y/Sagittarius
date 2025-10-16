package io.github.lazzz.sagittarius.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证授权服务启动类
 * 负责用户登录、注册、JWT令牌颁发与刷新等功能
 * @author Lazzz
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "io.github.lazzz.sagittarius.system.api")
@ComponentScan(basePackages = {"io.github.lazzz.sagittarius.auth", "io.github.lazzz.sagittarius.system.api"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}