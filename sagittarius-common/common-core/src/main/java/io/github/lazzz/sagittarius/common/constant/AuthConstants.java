package io.github.lazzz.sagittarius.common.constant;



/**
 * @className SecurityConstant 
 * @description TODO 
 * @author Lazzz 
 * @date 2025/09/19 23:34
**/
public interface AuthConstants {

    /**
     * 获RSA公钥 用于远程资源服务器与认证服务器之间的通信
     */
    String DEFAULT_GET_AUTH_RSA_PUBLIC_KEY_URL = "/rsa/auth";

    /**
     * 获RSA公钥 用户加密前端传过来的明文密码
     */
    String DEFAULT_GET_PASS_RSA_PUBLIC_KEY_URL = "/rsa/pass";

    // 定义 Knife4j 测试客户端的 ID，便于后续判断和维护
    String KNIFE4J_CLIENT_ID = "knife4j";
    String KNIFE4J_CLIENT_SECRET = "knife4j";

}

