package io.github.lazzz.sagittarius.common.redis.cache;

import io.github.lazzz.sagittarius.common.redis.annotation.CacheKeySuffix;
import lombok.Setter;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 缓存Key生成器
 *
 * @author Lazzz
 */
@Setter
public class TenantKeyGenerator implements KeyGenerator {

    /**
     * 组件间分隔符
     */
    private static final String SEPARATOR = ":";

    /**
     * 是否在key中包含类名（默认：true，避免不同类同名方法冲突）
     * -- SETTER --
     *  设置是否包含类名
     *
     * @param includeClassName true-包含类名，false-不包含
     */
    private boolean includeClassName = true;

    @Override
    public Object generate(Object target, Method method, Object... params) {

        // 构建key基础部分
        StringBuilder keyBuilder = new StringBuilder();

        // 添加类名（可选）
        if (includeClassName) {
            String className = target.getClass().getSimpleName();
            keyBuilder.append(className).append(SEPARATOR);
        }

        // 添加方法名
        keyBuilder.append(method.getName()).append(SEPARATOR);

        // 添加参数信息（处理多参数、空参数场景）
        String paramsStr = resolveParams(params);
        keyBuilder.append(paramsStr).append(SEPARATOR);

        CacheKeySuffix suffixAnnotation = method.getAnnotation(CacheKeySuffix.class);
        if (suffixAnnotation != null) {
            keyBuilder.append(suffixAnnotation.value());
        }

        return keyBuilder.toString();
    }

    /**
     * 解析参数为字符串（支持多参数、空参数、数组等场景）
     */
    private String resolveParams(Object... params) {
        if (params == null || params.length == 0) {
            return "noParams";
        }
        // 处理数组参数（如Object[]、List等）
        return Arrays.stream(params)
                .map(this::resolveSingleParam)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(SEPARATOR));
    }

    /**
     * 解析单个参数（可根据需要扩展，如处理复杂对象）
     */
    private String resolveSingleParam(Object param) {
        if (param == null) {
            return null;
        }
        // 数组类型特殊处理
        if (param.getClass().isArray()) {
            return Arrays.toString((Object[]) param);
        }
        // 基本类型直接toString，复杂对象可在此扩展（如JSON序列化）
        return param.toString();
    }

}