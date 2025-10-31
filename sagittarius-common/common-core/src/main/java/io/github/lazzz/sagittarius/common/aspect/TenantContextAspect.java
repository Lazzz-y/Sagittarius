package io.github.lazzz.sagittarius.common.aspect;


import io.github.lazzz.sagittarius.common.annotation.TenantAware;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 租户上下文切面
 * 
 * @author Lazzz 
 * @date 2025/10/10 14:58
**/
@Aspect
@Component
public class TenantContextAspect {

    @Around("@annotation(tenantAware)")
    public Object around(ProceedingJoinPoint joinPoint, TenantAware tenantAware) throws Throwable {
        // 获取租户ID
        Long tenantId = TenantContext.getTenantId();
        System.out.println("tenantId: " + tenantId);
        boolean needClear = false;
        if (tenantId != null) {
            // 在异步或新线程中重新设置上下文
            TenantContext.setTenantId(tenantId);
            needClear = true;
        }
        try {
            return joinPoint.proceed();
        } finally {
            if (needClear) {
                TenantContext.clear();
            }
        }
    }
}

