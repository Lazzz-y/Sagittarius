package io.github.lazzz.sagittarius.common.utils.condition;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 支持返回值的 If 扁平化工具
 *
 * @author Lazzz
 * @date 2025/9/27 00:26
 **/
public class ReturningIfFlattener<T, R> {
    
    private final T target;
    private R result = null;
    private boolean resultSet = false;
    
    private ReturningIfFlattener(T target) {
        this.target = target;
    }
    
    public static <T, R> ReturningIfFlattener<T, R> of(T target) {
        return new ReturningIfFlattener<>(target);
    }
    
    /**
     * 条件判断并设置返回值
     */
    public ReturningIfFlattener<T, R> when(boolean condition, Supplier<R> supplier) {
        if (!resultSet && condition) {
            this.result = supplier.get();
            this.resultSet = true;
        }
        return this;
    }
    
    /**
     * 带谓词的条件判断
     */
    public ReturningIfFlattener<T, R> when(Predicate<T> predicate, Supplier<R> supplier) {
        if (!resultSet && predicate.test(target)) {
            this.result = supplier.get();
            this.resultSet = true;
        }
        return this;
    }
    
    /**
     * 带谓词的条件判断（使用目标对象）
     */
    public ReturningIfFlattener<T, R> when(Predicate<T> predicate, Function<T, R> function) {
        if (!resultSet && predicate.test(target)) {
            this.result = function.apply(target);
            this.resultSet = true;
        }
        return this;
    }
    
    /**
     * 空值检查
     */
    public ReturningIfFlattener<T, R> whenNull(Supplier<R> supplier) {
        return when(Objects::isNull, supplier);
    }
    
    /**
     * 非空检查
     */
    public ReturningIfFlattener<T, R> whenNotNull(Function<T, R> function) {
        return when(Objects::nonNull, function);
    }
    
    /**
     * 否则设置返回值
     */
    public ReturningIfFlattener<T, R> otherwise(Supplier<R> supplier) {
        if (!resultSet) {
            this.result = supplier.get();
            this.resultSet = true;
        }
        return this;
    }
    
    /**
     * 否则设置返回值（使用目标对象）
     */
    public ReturningIfFlattener<T, R> otherwise(Function<T, R> function) {
        if (!resultSet) {
            this.result = function.apply(target);
            this.resultSet = true;
        }
        return this;
    }
    
    /**
     * 获取最终结果
     */
    public R get() {
        if (!resultSet) {
            throw new IllegalStateException("No condition matched and no otherwise provided");
        }
        return result;
    }
    
    /**
     * 获取最终结果或默认值
     */
    public R orElse(R defaultValue) {
        return resultSet ? result : defaultValue;
    }
}