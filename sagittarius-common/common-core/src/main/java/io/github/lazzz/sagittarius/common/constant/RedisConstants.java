package io.github.lazzz.sagittarius.common.constant;



/**
 * @interfaceName RedisConstant 
 * @description TODO 
 * @author Lazzz 
 * @date 2025/09/20 01:11
**/
public interface RedisConstants {

    /**
     * JWK缓存
     */
    String JWK_SET_KEY = "jwk_set";

    /**
     * 防重复提交锁前缀
     */
    String RESUBMIT_LOCK_PREFIX = "LOCK:RESUBMIT:";

    /**
     * 角色和权限缓存前缀
     */
    String ROLE_PERMS_PREFIX = "role_perms:";

    /**
     * 黑名单TOKEN Key前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    /**
     * 注册短信验证码key前缀
     */
    String REGISTER_SMS_CODE_PREFIX = "sms_code:register";

}

