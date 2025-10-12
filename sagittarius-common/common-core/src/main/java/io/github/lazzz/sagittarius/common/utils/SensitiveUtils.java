package io.github.lazzz.sagittarius.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;

public class SensitiveUtils {
    // 脱敏规则：保留前3位，后4位，中间用*代替（适用于手机号、身份证等）
    public static String desensitize(Object obj) {
        if (obj == null) return "null";
        // 使用 FastJSON 序列化时脱敏
        return JSON.toJSONString(obj, (ValueFilter) (object, name, value) -> {
            if (value == null) return null;
            String valueStr = value.toString();
            // 匹配敏感字段名（如 password、mobile、idCard 等）
            if (name.contains("password") || name.contains("pwd")) {
                return "******";
            }
            if (name.contains("mobile") || name.contains("phone")) {
                if (valueStr.length() == 11) {
                    return valueStr.substring(0, 3) + "****" + valueStr.substring(7);
                }
            }
            if (name.contains("idCard") || name.contains("idNo")) {
                if (valueStr.length() == 18) {
                    return valueStr.substring(0, 6) + "********" + valueStr.substring(14);
                }
            }
            return value;
        });
    }
}
