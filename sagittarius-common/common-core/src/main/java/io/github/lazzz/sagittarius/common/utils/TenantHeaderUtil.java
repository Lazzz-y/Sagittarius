package io.github.lazzz.sagittarius.common.utils;

import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 从请求头获取租户ID的工具类
 * @author Lazzz
 */
public class TenantHeaderUtil {

    /**
     * 从当前请求头中获取租户ID
     * @return 租户ID（Long类型）
     * @throws TenantNotFoundException 当请求头中无租户ID时抛出
     * @throws InvalidTenantIdException 当租户ID格式无效时抛出
     */
    public static Long getTenantIdFromHeader() {
        // 1. 获取当前请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new TenantNotFoundException("当前上下文不存在HTTP请求，无法获取租户ID");
        }
        HttpServletRequest request = requestAttributes.getRequest();

        // 2. 从上下文或请求头获取租户ID字符串
        String tenantIdStr = request.getAttribute(SystemConstants.TENANT_HEADER) != null ?
                request.getAttribute(SystemConstants.TENANT_HEADER).toString() :
                request.getHeader(SystemConstants.TENANT_HEADER);
        if (tenantIdStr == null || tenantIdStr.trim().isEmpty()) {
            // FIXME 开发环境临时处理，开发环境默认租户ID为1
            tenantIdStr = "1";
//            throw new TenantNotFoundException("请求头中未包含租户ID，请添加" + SystemConstants.TENANT_HEADER + "头信息");
        }

        // 3. 转换为Long类型并校验
        try {
            Long tenantId = Long.parseLong(tenantIdStr.trim());
            if (tenantId <= 0) {
                throw new InvalidTenantIdException("租户ID必须为正整数，当前值：" + tenantIdStr);
            }
            return tenantId;
        } catch (NumberFormatException e) {
            throw new InvalidTenantIdException("租户ID格式错误，必须为整数，当前值：" + tenantIdStr, e);
        }
    }

    /**
     * 自定义异常：租户ID未找到
     */
    public static class TenantNotFoundException extends RuntimeException {
        public TenantNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * 自定义异常：租户ID无效
     */
    public static class InvalidTenantIdException extends RuntimeException {
        public InvalidTenantIdException(String message) {
            super(message);
        }

        public InvalidTenantIdException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
