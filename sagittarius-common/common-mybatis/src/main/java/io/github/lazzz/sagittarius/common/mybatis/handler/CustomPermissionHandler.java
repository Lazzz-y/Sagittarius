package io.github.lazzz.sagittarius.common.mybatis.handler;


import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.common.security.util.SecurityUtils;
import io.github.lazzz.sagittarius.common.base.IBaseEnum;
import io.github.lazzz.sagittarius.common.mybatis.annotation.DataPermission;
import io.github.lazzz.sagittarius.common.mybatis.enums.DataScopeEnum;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 博客系统数据权限控制器
 *
 * @author Lazzz
 * @date 2025/09/22 21:12
 **/
@Slf4j
@Component
public class CustomPermissionHandler extends CommonsDialectImpl {

    @PostConstruct
    public void registerDialect() {
        // 注册自定义方言
        DialectFactory.registerDialect(DbType.MYSQL, this);
    }

    @Override
    public String buildSelectSql(QueryWrapper queryWrapper) {
        // 获取当前执行的 Mapper 方法
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().endsWith("Mapper")) {
                try {
                    // 获取 Mapper 接口类
                    Class<?> mapperClass = Class.forName(element.getClassName());
                    // 获取方法
                    Method[] methods = mapperClass.getDeclaredMethods();
                    for (Method method : methods) {
                        // 检查是否有 @DataPermission 注解
                        DataPermission annotation = method.getAnnotation(DataPermission.class);
                        if (annotation != null) {
                            // 添加数据权限过滤条件
                            addDataPermissionCondition(queryWrapper, annotation);
                            break;
                        }
                    }
                } catch (ClassNotFoundException e) {
                    log.warn("无法找到Mapper类: {}", element.getClassName());
                }
                break;
            }
        }
       return super.buildSelectSql(queryWrapper);
    }

    /**
     * 添加数据权限条件
     *
     * @param queryWrapper 查询条件
     * @param annotation 数据权限注解
     */
    public void addDataPermissionCondition(QueryWrapper queryWrapper, DataPermission annotation) {
        // 超级管理员不受数据权限控制
        if (SecurityUtils.isRoot()) {
            return;
        }

        // 获取当前用户的数据权限
        Integer dataScope = SecurityUtils.getDataScope();
        if (dataScope == null) {
            return;
        }

        DataScopeEnum dataScopeEnum = IBaseEnum.getEnumByValue(dataScope, DataScopeEnum.class);

        String userIdColumnName = annotation.userIdColumnName();
        String userAlias = annotation.userAlias();

        // 构建完整的列名
        String fullColumnName = StrUtil.isNotBlank(userAlias)
                ? userAlias + "." + userIdColumnName
                : userIdColumnName;

        switch (Objects.requireNonNull(dataScopeEnum)) {
            case ALL:
                // 不添加任何条件，直接返回
                break;
            case SELF:
                Long userId = SecurityUtils.getUserId();
                if (userId != null) {
                    queryWrapper.and(fullColumnName + " = " + userId);
                }
                break;
            default:
                // 默认只能查看自己的数据
                Long defaultUserId = SecurityUtils.getUserId();
                if (defaultUserId != null) {
                    queryWrapper.and(fullColumnName + " = " + defaultUserId);
                }
        }
    }

}

