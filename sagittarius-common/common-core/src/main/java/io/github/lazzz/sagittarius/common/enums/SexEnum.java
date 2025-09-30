package io.github.lazzz.sagittarius.common.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import io.github.lazzz.sagittarius.common.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author Lazzz
 * @date 2025/09/29 19:39
 **/
@AllArgsConstructor
public enum SexEnum implements IBaseEnum<Integer> {

    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(0, "未知");

    @Getter
    @EnumValue
    private Integer value;

    @Getter
    @JsonValue
    private String label;

}

