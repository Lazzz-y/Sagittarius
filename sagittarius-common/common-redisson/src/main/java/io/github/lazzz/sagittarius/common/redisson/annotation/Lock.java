package io.github.lazzz.sagittarius.common.redisson.annotation;

import io.github.lazzz.sagittarius.common.redisson.model.LockStrategy;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Redisson 锁注解
 *
 * @author Lazzz
 * @date 2025/10/19 21:14
 **/
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Lock {
    /**
     * 锁的名称
     */
    String name() default "";

    /**
     * 尝试加锁，最多等待时间
     */
    long waitTime() default 5;

    /**
     * 上锁以后xxx秒自动解锁
     */
    long leaseTime() default 30;

    /**
     * 锁等待时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 锁类型，默认可重入锁
     */
    LockType lockType() default LockType.REENTRANT;

    LockStrategy lockStrategy() default LockStrategy.FAIL_AFTER_RETRY_TIMEOUT;

    /**
     * 自定义业务key
     */
    String[] keys() default {};
}