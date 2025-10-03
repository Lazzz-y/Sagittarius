package io.github.lazzz.sagittarius.common.mybatis.config;


import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * TODO
 *
 * @author Lazzz
 * @date 2025/10/03 12:43
**/
public class CustomTenantFactory implements TenantFactory {
    @Override
    public Object[] getTenantIds() {
        return new Object[0];
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        Long tenantId = (Long) attributes.getAttribute("tenantId", RequestAttributes.SCOPE_REQUEST);

        return new Object[]{tenantId};
    }
}

