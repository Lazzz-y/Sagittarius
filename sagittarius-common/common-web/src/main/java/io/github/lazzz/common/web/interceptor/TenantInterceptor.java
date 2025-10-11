package io.github.lazzz.common.web.interceptor;


import cn.hutool.core.util.StrUtil;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 租户识别拦截器
 *
 * @author Lazzz
 * @date 2025/10/03 16:06
**/
@Component
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

        String tenantIdStr = request.getHeader(SystemConstants.TENANT_HEADER);
        long tenantId = 1L;
        if (StrUtil.isNotBlank(tenantIdStr)){
            try {
                tenantId = Long.parseLong(tenantIdStr);
            } catch (NumberFormatException e){
                throw new IllegalArgumentException("Invalid tenant ID: " + tenantIdStr);
            }
        }
        TenantContext.setTenantId(tenantId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
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

