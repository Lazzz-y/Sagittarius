package io.github.lazzz.sagittarius.common.utils;

/**
 * 租户ID上下文
 *
 * @author Lazzz
 * @date 2025/10/10 14:46
 **/
public class TenantContext {
       private static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();
       
       public static void setTenantId(Long tenantId) {
           CONTEXT.set(tenantId);
       }
       
       public static Long getTenantId() {
           return CONTEXT.get();
       }
       
       public static void clear() {
           CONTEXT.remove();
       }
   }
   