package io.github.lazzz.sagittarius.common.utils.condition;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * If 扁平化快捷工具
 *
 * @author Lazzz
 * @date 2025/9/27 00:27
 **/
public final class If {

    private If() {
    }

    /**
     * 基础条件判断
     */
    public static Conditional ifThen(boolean condition, Runnable action) {
        return new Conditional(condition, action);
    }

    /**
     * 对象条件判断
     */
    public static <T> ObjectConditional<T> ifThen(T target, Predicate<T> condition, Consumer<T> action) {
        return new ObjectConditional<>(target, condition, action);
    }

    public static <T> ObjectConditional<T> withNotBlank(T target, Consumer<T> action) {
        return new ObjectConditional<>(target, t -> t instanceof String str && StrUtil.isNotBlank(str), action);
    }

    public static <T> ObjectConditional<T> withBlank(T target, Consumer<T> action) {
        return new ObjectConditional<>(target, t -> t instanceof String str && StrUtil.isBlank(str), action);
    }

    public static <T> ObjectConditional<T> withNotEmpty(T target, Consumer<T> action) {
        return new ObjectConditional<>(target,
                t -> (t instanceof java.util.Collection<?> col && CollectionUtil.isNotEmpty(col)) ||
                        (t instanceof java.util.Map<?, ?> map && CollectionUtil.isNotEmpty(map))
                , action);
    }

    public static <T> ObjectConditional<T> withEmpty(T target, Consumer<T> action) {
        return new ObjectConditional<>(target,
                t -> (t instanceof java.util.Collection<?> col && CollectionUtil.isEmpty(col)) ||
                        (t instanceof java.util.Map<?, ?> map && CollectionUtil.isEmpty(map))
                , action);
    }

    public static <T> ObjectConditional<T> withNotNull(T target, Consumer<T> action) {
        return new ObjectConditional<>(target, ObjectUtil::isNotNull, action);
    }

    public static <T> ObjectConditional<T> withNull(T target, Consumer<T> action) {
        return new ObjectConditional<>(target, ObjectUtil::isNull, action);
    }

    /**
     * 条件链式处理器
     */
    public static class Conditional {
        private boolean matched;

        private Conditional(boolean condition, Runnable action) {
            if (condition) {
                action.run();
                this.matched = true;
            }
        }

        public Conditional elseIf(boolean condition, Runnable action) {
            if (!matched && condition) {
                action.run();
                this.matched = true;
            }
            return this;
        }

        public void elseThen(Runnable action) {
            if (!matched) {
                action.run();
            }
        }
    }

    /**
     * 对象条件链式处理器
     */
    public static class ObjectConditional<T> {
        private final T target;
        private boolean matched;

        private ObjectConditional(T target, Predicate<T> condition, Consumer<T> action) {
            this.target = target;
            if (condition.test(target)) {
                action.accept(target);
                this.matched = true;
            }
        }

        public ObjectConditional<T> elseIf(Predicate<T> condition, Consumer<T> action) {
            if (!matched && condition.test(target)) {
                action.accept(target);
                this.matched = true;
            }
            return this;
        }

        public void elseThen(Consumer<T> action) {
            if (!matched) {
                action.accept(target);
            }
        }
    }
}