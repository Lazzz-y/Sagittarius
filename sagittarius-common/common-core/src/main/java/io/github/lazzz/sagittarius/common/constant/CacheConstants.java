package io.github.lazzz.sagittarius.common.constant;


import io.github.lazzz.sagittarius.common.utils.TenantContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Redis常量
 * @author Lazzz 
 * @date 2025/09/20 01:11
**/
public interface CacheConstants {

    String DEFAULT_AREA = "default";

    /**
     * JWK缓存
     * 启动的时候需要初始化，而启动时间无法获取租户ID，所以不使用租户id区分
     */
    String JWK_SET_KEY = "jwk_set";

    /**
     * 防重复提交锁前缀
     */
    String RESUBMIT_LOCK_PREFIX = TenantContext.getTenantId() + ":" + "LOCK:RESUBMIT:";

    /**
     * 角色和权限缓存前缀
     */
    String ROLE_PERMS_PREFIX = TenantContext.getTenantId() + ":" + "role_perms";

    /**
     * 黑名单TOKEN Key前缀
     * 租户ID在全局过滤器中拼接
     */
    String TOKEN_BLACKLIST_PREFIX = TenantContext.getTenantId() + ":" + "token:blacklist:";

    /**
     * 注册短信验证码key前缀
     */
    String REGISTER_SMS_CODE_PREFIX = TenantContext.getTenantId() + "sms_code:register:";

    String DICT_PREFIX = TenantContext.getTenantId() + ":dict:";

    /////////////////// SpEl 表达式 生成 ///////////////////

    String TENANT_ID = "T(io.github.lazzz.sagittarius.common.utils.TenantContext).getTenantId() + ':' + ";

    String SPEL_MENU_KEY = TENANT_ID + "':route'";

    String SPEL_DICT_FORM_TYPE_CODE_KEY = TENANT_ID + "'dict:' + #form.typeCode";


    static String parseSpEl(String spEl, Object rootObject) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
        return parser.parseExpression(spEl).getValue(context, String.class);
    }

}

