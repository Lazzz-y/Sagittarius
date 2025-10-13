package io.github.lazzz.sagittarius.common.constant;



/**
 * 系统常量
 * @author Lazzz 
 * @date 2025/09/27 16:37
**/
public interface SystemConstants {

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 超级管理员角色代码
     */
    String ROOT_ROLE_CODE = "SUPER_ADMIN";

    /**
     * 租户ID请求头
     */
    String TENANT_HEADER = "X-Tenant-Id";

    /**
     * 默认用户角色ID
     */
    Long DEFAULT_USER_ROLE_ID = 231351564713857027L;

    /**
     * 默认租户ID
     */
    Long DEFAULT_TENANT_ID = 1L;

    /**
     * 默认租户ID字符串
     */
    String DEFAULT_TENANT_ID_STR = "1";

    /**
     * 根菜单ID
     */
    Long ROOT_NODE_ID = 0L;
}

