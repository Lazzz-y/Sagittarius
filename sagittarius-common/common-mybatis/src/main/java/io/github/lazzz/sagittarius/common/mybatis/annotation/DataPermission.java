package io.github.lazzz.sagittarius.common.mybatis.annotation;


import java.lang.annotation.*;

/**
 * 数据权限注解
 *
 * @author Lazzz
 * @date 2025/09/22 21:41
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    /**
     * 用户字段别名
     */
    String userAlias() default "u";

    /**
     * 用户ID字段名称
     */
    String userIdColumnName() default "create_by";

}
