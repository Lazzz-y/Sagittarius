package io.github.lazzz.sagittarius.common.enums;


import io.github.lazzz.sagittarius.common.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author Lazzz
 * @date 2025/09/29 21:09
 **/
@AllArgsConstructor
public enum UserTypeEnum implements IBaseEnum<Integer> {

    NORMAL(0, "普通用户"),
    AUTHOR(1, "博客作者"),
    ADMIN(2, "内容管理员");


    @Getter
    private Integer value;

    @Getter
    private String label;

}

