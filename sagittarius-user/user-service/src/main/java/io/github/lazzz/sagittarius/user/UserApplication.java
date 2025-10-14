package io.github.lazzz.sagittarius.user;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户服务启动类
 * 负责用户信息管理（CRUD）、个人设置功能等
 * @author Lazzz (<a href="https://github.com/Lazzz-y">Github</a>)
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableMethodCache(basePackages = "io.github.lazzz.sagittarius")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}