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

}

