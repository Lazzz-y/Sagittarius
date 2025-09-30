package io.github.lazzz.sagittarius.common.utils.condition;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Predicate;

/**
 * If 扁平化工具类
 * 支持链式调用，避免深层嵌套的 if-else 语句
 *
 * @author Lazzz
 * @date 2025/9/27 00:24
 **/
public class IfFlattener<T> {
    
    private final T target;
    private boolean conditionMatched = false;
    private Runnable elseAction = null;
    
    private IfFlattener(T target) {
        this.target = target;
    }
    
    /**
     * 创建 IfFlattener 实例
     */
    public static <T> IfFlattener<T> of(T target) {
        return new IfFlattener<>(target);
    }
    
    /**
     * 创建无目标对象的 IfFlattener 实例
     */
    public static IfFlattener<Void> of() {
        return new IfFlattener<>(null);
    }
    
    /**
     * 条件判断
     */
    public IfFlattener<T> when(boolean condition, Consumer<T> action) {
        if (!conditionMatched && condition) {
            action.accept(target);
            conditionMatched = true;
        }
        return this;
    }
    
    /**
     * 带谓词的条件判断
     */
    public IfFlattener<T> when(Predicate<T> predicate, Consumer<T> action) {
        if (!conditionMatched && predicate.test(target)) {
            action.accept(target);
            conditionMatched = true;
        }
        return this;
    }
    
    /**
     * 空值检查
     */
    public IfFlattener<T> whenNull(Consumer<T> action) {
        return when(Objects::isNull, action);
    }
    
    /**
     * 非空检查
     */
    public IfFlattener<T> whenNotNull(Consumer<T> action) {
        return when(Objects::nonNull, action);
    }
    
    /**
     * 相等性检查
     */
    public IfFlattener<T> whenEquals(T expected, Consumer<T> action) {
        return when(obj -> Objects.equals(obj, expected), action);
    }
    
    /**
     * 字符串非空检查
     */
    public IfFlattener<T> whenNotBlank(Consumer<T> action) {
        return when(obj -> obj instanceof String && !((String) obj).trim().isEmpty(), action);
    }
    
    /**
     * 集合非空检查
     */
    public IfFlattener<T> whenNotEmpty(Consumer<T> action) {
        return when(obj -> {
            if (obj instanceof java.util.Collection) {
                return !((java.util.Collection<?>) obj).isEmpty();
            }
            if (obj instanceof java.util.Map) {
                return !((java.util.Map<?, ?>) obj).isEmpty();
            }
            return false;
        }, action);
    }
    
    /**
     * 否则执行
     */
    public IfFlattener<T> otherwise(Consumer<T> action) {
        if (!conditionMatched) {
            action.accept(target);
        }
        return this;
    }
    
    /**
     * 否则执行（无参数）
     */
    public IfFlattener<T> otherwise(Runnable action) {
        if (!conditionMatched) {
            action.run();
        }
        return this;
    }
    
    /**
     * 获取结果（用于有返回值的场景）
     */
    public <R> R result(Supplier<R> defaultSupplier) {
        return defaultSupplier.get();
    }
    
    /**
     * 结束处理，返回原始对象
     */
    public T end() {
        return target;
    }
}