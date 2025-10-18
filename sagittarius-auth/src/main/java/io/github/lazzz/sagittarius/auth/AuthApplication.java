package io.github.lazzz.sagittarius.auth;

import io.github.lazzz.sagittarius.common.utils.AnsiColor;
import io.github.lazzz.sagittarius.common.utils.IPUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StopWatch;

import java.util.Objects;

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
        startup(args);
    }

    private static void startup(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SpringApplication.run(AuthApplication.class, args);
        stopWatch.stop();

        print(stopWatch.getTotalTimeMillis());
    }

    private static void print(Long ms) {
        Runtime runtime = Runtime.getRuntime();
        String totalMemory = runtime.maxMemory() / 1024 / 1024 + "M";
        String freeMemory = runtime.freeMemory() / 1024 / 1024 + "M";

        // 服务配置信息
        String serviceName = "Auth (认证服务)";
        String host = Objects.requireNonNull(IPUtils.getLocalHostExactAddress()).getHostAddress();
        String port = "8801";
        String baseUrl = "http://" + host + ":" + port;
        String docUrl = "http://" + host + ":" + 8800 + "/doc.html";

        // 格式化时间
        String startTime = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        // 日志内容
        System.out.println(AnsiColor.WHITE + "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + AnsiColor.RESET);
        System.out.println(AnsiColor.BOLD_GREEN + "                       ✅ 服务启动成功 - 系统就绪" + AnsiColor.RESET);
        System.out.println(AnsiColor.WHITE + "┃━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┃" + AnsiColor.RESET);
        System.out.println(AnsiColor.BLUE_LIGHT + "  服务名称   : " + AnsiColor.BOLD_YELLOW + serviceName + AnsiColor.RESET);
        System.out.println(AnsiColor.BLUE_LIGHT + "  启动时间   : " + AnsiColor.GRAY_300 + startTime + AnsiColor.RESET);
        System.out.println(AnsiColor.WHITE + "┃━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┃" + AnsiColor.RESET);
        System.out.println(AnsiColor.BRIGHT_RED + "  最大内存   : " + AnsiColor.BOLD + AnsiColor.BRIGHT_RED + totalMemory + AnsiColor.RESET);
        System.out.println(AnsiColor.BRIGHT_GREEN + "  可用内存   : " + AnsiColor.BOLD + AnsiColor.BRIGHT_GREEN + freeMemory + AnsiColor.RESET);
        System.out.println(AnsiColor.WHITE + "┃━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┃" + AnsiColor.RESET);
        System.out.println(AnsiColor.BLUE_LIGHT + "  访问地址   : " + AnsiColor.LINK_STYLE + baseUrl + AnsiColor.RESET);
        System.out.println(AnsiColor.BLUE_LIGHT + "  文档地址   : " + AnsiColor.LINK_STYLE + docUrl + AnsiColor.RESET);
        System.out.println(AnsiColor.BLUE_LIGHT + "  运行端口   : " + AnsiColor.GRAY_300 + port + " (" + host + ")" + AnsiColor.RESET);
        System.out.println(AnsiColor.BLUE_LIGHT + "  启动耗时   : " + AnsiColor.GRAY_300 + ms + " ms" + AnsiColor.RESET);
        System.out.println(AnsiColor.WHITE + "┃━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┃" + AnsiColor.RESET);
        System.out.println(AnsiColor.GREEN + "             🚀 状态: 所有依赖加载完成，服务已进入可用状态" + AnsiColor.RESET);
        System.out.println(AnsiColor.WHITE + "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + AnsiColor.RESET);
    }
}