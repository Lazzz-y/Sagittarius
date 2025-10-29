package io.github.lazzz.sagittarius.dict.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据字典注解
 *
 * @author Tellsea
 * @date 2020/6/23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
 
  /**
   * 字典类型编码
   */
  String typeCode();

  /**
   * 源字段名（存储字典编码的字段，默认当前字段）
   */
  String sourceField() default "";

  /**
   * 目标字段名（存储字典名称的字段，默认源字段名 + "Name"）
   */
  String targetField() default "";

}