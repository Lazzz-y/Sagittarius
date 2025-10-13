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

    NULL(0, null),
    MENU(1, "菜单"),
    CATALOG(2, "目录"),
    BUTTON(3, "按钮"),
    EXTLINK(4, "外链"),
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

