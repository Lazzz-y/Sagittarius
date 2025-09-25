package io.github.lazzz.common.web.annotation;


import java.lang.annotation.*;

/**
 * 防止重复提交注解
 * @author Lazzz
 * @date 2025/09/22 22:46
 **/
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicateResubmit {

    /**
     * 防重提交锁过期时间(秒)
     * <p>
     * 默认5秒内不允许重复提交
     */
    int expire() default 5;

}
