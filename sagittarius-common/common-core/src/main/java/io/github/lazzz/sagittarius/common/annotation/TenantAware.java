package io.github.lazzz.sagittarius.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异步和非Web环境自动传递租户Id注解
 *
 * @author Lazzz
 * @date 2025/10/10 14:56
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantAware {
}
