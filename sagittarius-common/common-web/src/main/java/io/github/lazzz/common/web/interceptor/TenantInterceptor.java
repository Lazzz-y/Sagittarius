package io.github.lazzz.common.web.interceptor;


import io.github.lazzz.sagittarius.common.utils.TenantHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *  TODO
 * 
 * @author Lazzz 
 * @date 2025/10/03 16:06
**/
public class TenantInterceptor implements HandlerInterceptor {

    // 定义需要排除的Knife4j路径
    private static final String[] EXCLUDE_PATHS = {
            "/doc.html",
            "/webjars/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/favicon.ico"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();

        // 排除 Knife4j 路径
        if (isExcludePath(requestUri)){
            return true;
        }

        Long tenantId = request.getAttribute("tenantId") != null ? (Long) request.getAttribute("tenantId") : TenantHeaderUtil.getTenantIdFromHeader();
        // 设置到上下文（自动校验合法性，抛出异常则请求终止）
        request.setAttribute("tenantId", tenantId);
        return true;
    }

    private boolean isExcludePath(String path) {
        for (String excludePath : EXCLUDE_PATHS) {
            if (excludePath.endsWith("/**")) {
                String basePath = excludePath.substring(0, excludePath.length() - 3);
                if (path.startsWith(basePath)) {
                    return true;
                }
            } else if (path.equals(excludePath)) {
                return true;
            }
        }
        return false;
    }

}

