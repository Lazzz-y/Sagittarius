package io.github.lazzz.sagittarius.common.base;


import cn.hutool.core.util.ObjectUtil;

import java.util.EnumSet;

/**
 * 枚举通用接口
 *
 * @author Lazzz
 * @date 2025/9/22 21:29
 **/
public interface IBaseEnum<T> {

    T getValue();

    String getLabel();

    static <E extends Enum<E> & IBaseEnum> E getEnumByName(String name, Class<E> clazz) {
        if (name == null) {
            return null;
        }
        return EnumSet.allOf(clazz).stream()
                .filter(e -> ObjectUtil.equal(e.name(), name))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据文本标签获取枚举
     *
     * @param label
     * @param clazz
     * @param <E>   枚举
     * @return
     */
    static <E extends Enum<E> & IBaseEnum> E getEnumByLabel(String label, Class<E> clazz) {

        if (label == null) {
            return null;
        }

        // 获取类型下的所有枚举
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        return allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getLabel(), label))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据值获取枚举
     *
     * @param value
     * @param clazz
     * @param <E>   枚举
     * @return
     */
    static <E extends Enum<E> & IBaseEnum> E getEnumByValue(Object value, Class<E> clazz) {

        if (value == null) {
            return null;
        }

        // 获取类型下的所有枚举
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        return allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据文本标签获取值
     *
     * @param value
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IBaseEnum> String getLabelByValue(Object value, Class<E> clazz) {
        if (value == null) {
            return null;
        }

        // 获取类型下的所有枚举
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        E matchEnum = allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst()
                .orElse(null);

        String label = null;
        if (matchEnum != null) {
            label = matchEnum.getLabel();
        }
        return label;
    }


    /**
     * 根据文本标签获取值
     *
     * @param label
     * @param clazz
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IBaseEnum> Object getValueByLabel(String label, Class<E> clazz) {
        if (label == null) {
            return null;
        }

        // 获取类型下的所有枚举
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        String finalLabel = label;
        E matchEnum = allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getLabel(), finalLabel))
                .findFirst()
                .orElse(null);

        Object value = null;
        if (matchEnum != null) {
            value = matchEnum.getValue();
        }
        return value;
    }


}
