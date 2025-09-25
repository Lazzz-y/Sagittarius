package io.github.lazzz.sagittarius.common.mybatis.enums;


import io.github.lazzz.sagittarius.common.base.IBaseEnum;
import lombok.Getter;

/**
 * 博客系统数据权限枚举
 *
 * @author Lazzz
 * @date 2025/09/22 21:08
 **/
public enum DataScopeEnum implements IBaseEnum<Integer> {

    /**
     * value 越小，数据权限范围越大
     */
    ALL(0, "所有数据"),
    SELF(1, "本人数据")
    ;
    @Getter
    private final Integer value;

    @Getter
    private final String label;

    DataScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

}

