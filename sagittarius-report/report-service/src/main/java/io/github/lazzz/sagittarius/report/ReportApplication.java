package io.github.lazzz.sagittarius.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 报表服务启动类
 * 负责任务统计、报表生成和数据导出等功能
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }

}