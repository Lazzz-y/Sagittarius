package io.github.lazzz.sagittarius.common.mybatis.config;


import com.mybatisflex.core.tenant.TenantFactory;
import io.github.lazzz.sagittarius.common.utils.TenantContext;

import java.util.Set;

/**
 * TODO
 *
 * @author Lazzz
 * @date 2025/10/03 12:43
 **/
public class CustomTenantFactory implements TenantFactory {

    private static final Set<String> IGNORE_TABLES = Set.of("sys_tenant");


    @Override
    public Object[] getTenantIds() {
        return new Object[]{TenantContext.getTenantId()};
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        // 针对特定表自定义租户ID
        if (IGNORE_TABLES.contains(tableName)){
            return new Object[0];
        }
        return getTenantIds();
    }
}

