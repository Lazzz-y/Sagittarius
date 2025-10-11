package io.github.lazzz.sagittarius.common.annotation;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 带刷新 Nacos 配置的 Controller 注解
 *
 * @author Lazzz
 * @date 2025/10/10 15:29
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RefreshScope
@RestController
public @interface RefreshableController {
    // 组合注解，同时包含RefreshScope和RestController
}
