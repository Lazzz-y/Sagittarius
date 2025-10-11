package io.github.lazzz.sagittarius.common.result;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果状态码
 *
 * @author Lazzz
 * @date 2025/09/19 16:33
 **/
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode implements IResultCode, Serializable {

    SUCCESS("00000", "ok"),

    // User + Code (U0000)
    TOKEN_INVALID("U0230", "token无效或已过期"),
    TOKEN_ACCESS_FORBIDDEN("U0231", "token已被禁止访问"),

    ACCESS_UNAUTHORIZED("U0301", "访问未授权"),
    REPEAT_SUBMIT_ERROR("U0303", "您的请求已提交，请不要重复提交或等待片刻再尝试。"),


    // System + Code (S0000)
    FLOW_LIMITING("S0210", "系统限流"),
    SYSTEM_EXECUTION_ERROR("S0211", "系统执行出错"),
    SYSTEM_EXECUTION_TIMEOUT("S0212", "系统执行超时"),

    AUTHORIZED_ERROR("S0300", "访问权限异常"),
    FORBIDDEN_OPERATION("S0302", "演示环境禁止新增、修改和删除重要数据，请本地部署后测试"),

    PARAM_ERROR("S0400", "用户请求参数错误"),
    RESOURCE_NOT_FOUND("S0401", "请求资源不存在"),
    PARAM_IS_NULL("S0410", "请求必填参数为空"),
    ;

    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }

}

