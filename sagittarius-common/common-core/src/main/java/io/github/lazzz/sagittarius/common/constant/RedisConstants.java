package io.github.lazzz.sagittarius.common.constant;


import io.github.lazzz.sagittarius.common.utils.TenantContext;

/**
 * Redis常量
 * @author Lazzz 
 * @date 2025/09/20 01:11
**/
public interface RedisConstants {

    /**
     * JWK缓存
     * 启动的时候需要初始化，而启动时间无法获取租户ID，所以不使用租户id区分
     */
    String JWK_SET_KEY = "jwk_set:";

    /**
     * 防重复提交锁前缀
     */
    String RESUBMIT_LOCK_PREFIX = "LOCK:RESUBMIT:" + TenantContext.getTenantId();

    /**
     * 角色和权限缓存前缀
     */
    String ROLE_PERMS_PREFIX = "role_perms:" + TenantContext.getTenantId();

    /**
     * 黑名单TOKEN Key前缀
     * 租户ID在全局过滤器中拼接
     */
    String TOKEN_BLACKLIST_PREFIX = "token:blacklist:" + TenantContext.getTenantId() + ":";

    /**
     * 注册短信验证码key前缀
     */
    String REGISTER_SMS_CODE_PREFIX = "sms_code:register:" + TenantContext.getTenantId();

}

