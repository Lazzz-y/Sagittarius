package io.github.lazzz.common.security.enums;


import io.github.lazzz.sagittarius.common.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *
 * @author Lazzz
 * @date 2025/09/30 20:36
 **/
@AllArgsConstructor
public enum RoleScopeEnum implements IBaseEnum<Integer> {
    SUPER_ADMIN(1, "SUPER_ADMIN")
    , ADMIN(2, "ADMIN")
    , USER(3, "USER")
    , GUEST(4, "GUEST")
    ;

    @Getter
    private Integer value;
    @Getter
    private String label;


    /**
     * 判断当前角色是否比目标角色优先级更高
     *
     * @param target 角色
     * @return true=当前角色优先级更高，false=否则
     */
    public boolean isHigherThan(RoleScopeEnum target) {
        return this.value < target.value;
    }

}

