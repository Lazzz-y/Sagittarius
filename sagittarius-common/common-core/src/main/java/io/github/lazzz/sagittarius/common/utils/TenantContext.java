package io.github.lazzz.sagittarius.common.utils;

import java.util.Objects;

/**
 * 租户上下文工具类：
 * 1. 基于ThreadLocal存储当前线程的租户信息（租户ID、租户Key）
 * 2. 支持子线程继承（InheritableThreadLocal），适配异步任务场景
 * 3. 提供严格的非空校验和异常处理
 */
public class TenantContext {

    /**
     * 存储租户ID（Long类型，全局唯一标识）
     * 使用InheritableThreadLocal：子线程可继承父线程的租户ID（如@Async异步任务）
     */
    private static final ThreadLocal<Long> TENANT_ID = new InheritableThreadLocal<>();

    /**
     * 存储租户Key（String类型，业务唯一标识，如"blog_enterprise_a"）
     * 用于需要业务含义的场景（如域名解析、日志打印）
     */
    private static final ThreadLocal<String> TENANT_KEY = new InheritableThreadLocal<>();


    // -------------------------- 租户ID操作 --------------------------

    /**
     * 设置当前线程的租户ID
     * @param tenantId 租户ID（必须为正数，否则抛出异常）
     */
    public static void setTenantId(Long tenantId) {
        if (tenantId == null || tenantId <= 0) {
            throw new InvalidTenantException("租户ID必须为正数，当前值：" + tenantId);
        }
        TENANT_ID.set(tenantId);
    }

    /**
     * 获取当前线程的租户ID（带非空校验）
     * @return 租户ID（Long）
     * @throws TenantNotSetException 当租户ID未设置时抛出
     */
    public static Long getTenantId() {
        Long tenantId = TENANT_ID.get();
        if (tenantId == null) {
            throw new TenantNotSetException("当前线程未设置租户ID，请先调用setTenantId()");
        }
        return tenantId;
    }

    /**
     * 安全获取租户ID（无异常，返回null）
     * 用于非核心流程，允许租户ID为空的场景（如系统级操作）
     */
    public static Long getTenantIdOrNull() {
        return TENANT_ID.get();
    }


    // -------------------------- 租户Key操作 --------------------------

    /**
     * 设置当前线程的租户Key
     * @param tenantKey 租户业务标识（如"blog_enterprise_a"）
     */
    public static void setTenantKey(String tenantKey) {
        if (tenantKey == null || tenantKey.trim().isEmpty()) {
            throw new InvalidTenantException("租户Key不能为空或空白字符串");
        }
        TENANT_KEY.set(tenantKey.trim());
    }

    /**
     * 获取当前线程的租户Key（带非空校验）
     * @return 租户Key（String）
     * @throws TenantNotSetException 当租户Key未设置时抛出
     */
    public static String getTenantKey() {
        String tenantKey = TENANT_KEY.get();
        if (tenantKey == null) {
            throw new TenantNotSetException("当前线程未设置租户Key，请先调用setTenantKey()");
        }
        return tenantKey;
    }

    /**
     * 安全获取租户Key（无异常，返回null）
     */
    public static String getTenantKeyOrNull() {
        return TENANT_KEY.get();
    }


    // -------------------------- 上下文管理 --------------------------

    /**
     * 清除当前线程的所有租户上下文（必须在请求结束时调用）
     * 防止ThreadLocal内存泄漏（尤其是线程池复用场景）
     */
    public static void clear() {
        TENANT_ID.remove();
        TENANT_KEY.remove();
    }

    /**
     * 判断当前线程是否已设置租户ID
     */
    public static boolean hasTenantId() {
        return Objects.nonNull(TENANT_ID.get());
    }

    /**
     * 复制当前线程的租户上下文到目标线程（用于线程池提交任务时手动传递）
     * @param targetThread 目标线程
     */
    public static void copyTo(Thread targetThread) {
        // 实际项目中需配合线程池工具类使用，此处为简化示例
        Long currentTenantId = getTenantIdOrNull();
        String currentTenantKey = getTenantKeyOrNull();

        if (currentTenantId != null) {
            // 向目标线程的ThreadLocal设置当前租户ID（伪代码，实际需通过线程池包装实现）
            // targetThreadLocal.set(currentTenantId);
        }
        // 租户Key同理
    }


    // -------------------------- 自定义异常 --------------------------

    /**
     * 租户未设置异常：当未调用set直接get时抛出
     */
    public static class TenantNotSetException extends RuntimeException {
        public TenantNotSetException(String message) {
            super(message);
        }
    }

    /**
     * 租户信息无效异常：设置的租户ID/Key不符合规则时抛出
     */
    public static class InvalidTenantException extends RuntimeException {
        public InvalidTenantException(String message) {
            super(message);
        }
    }
}
