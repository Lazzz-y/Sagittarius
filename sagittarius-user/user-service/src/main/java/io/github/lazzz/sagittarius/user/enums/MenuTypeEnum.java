package io.github.lazzz.sagittarius.user.enums;


import com.mybatisflex.annotation.EnumValue;
import io.github.lazzz.sagittarius.common.base.IBaseEnum;
import lombok.Getter;

/**
 * 菜单类型枚举
 * 
 * @author Lazzz 
 * @date 2025/10/10 17:23
**/
public enum MenuTypeEnum implements IBaseEnum<Integer> {

    NULL(0, "无"),
    MENU(1, "菜单"),
    BUTTON(2, "按钮"),
    EXTLINK(3, "外链"),
    ;

    @Getter
    @EnumValue
    private Integer value;

    @Getter
    private String label;

    MenuTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

}

