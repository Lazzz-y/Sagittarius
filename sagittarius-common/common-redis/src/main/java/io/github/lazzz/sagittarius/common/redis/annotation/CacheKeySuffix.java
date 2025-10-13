package io.github.lazzz.sagittarius.common.redis.annotation;


import java.lang.annotation.*;

/**
 * 缓存Key后缀
 *
 * @author Lazzz
 * @date 2025/10/13 19:58
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheKeySuffix {

    /* 需要拼接的后缀 */
    String value() default "";
}
