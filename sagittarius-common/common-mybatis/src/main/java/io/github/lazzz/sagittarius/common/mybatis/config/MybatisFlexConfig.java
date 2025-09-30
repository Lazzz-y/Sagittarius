package io.github.lazzz.sagittarius.common.mybatis.config;


import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.MessageCollector;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import io.github.lazzz.sagittarius.common.mybatis.handler.CustomPermissionHandler;
import io.github.lazzz.sagittarius.common.mybatis.handler.IntegerArrayJsonTypeHandler;
import io.github.lazzz.sagittarius.common.mybatis.handler.LongObjectJsonTypeHandler;
import io.github.lazzz.sagittarius.common.mybatis.handler.StringObjectJsonTypeHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisFlex 配置
 *
 * @author Lazzz 
 * @date 2025/09/21 12:20
**/
@Slf4j
@Configuration
@EnableTransactionManagement
public class MybatisFlexConfig implements MyBatisFlexCustomizer {

    private final CustomPermissionHandler customPermissionHandler;

    private final ApplicationContext applicationContext;

    public MybatisFlexConfig(CustomPermissionHandler customPermissionHandler, ApplicationContext applicationContext) {
        this.customPermissionHandler = customPermissionHandler;
        this.applicationContext = applicationContext;
    }

    @Override
    public void customize(FlexGlobalConfig config) {
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(msg ->
                log.info("当前SQL: {},\n总耗时: {}ms", msg.getFullSql(), msg.getElapsedTime())
                );
    }

    @PostConstruct
    public void configureDialect(){
        // 注册自定义方言(包含数据权限处理)
        DialectFactory.registerDialect(DbType.MYSQL, customPermissionHandler);
    }

    @PostConstruct
    public void configureTypeHandlers() {
        TypeHandlerRegistry registry = new TypeHandlerRegistry();
        registry.register(String[].class, new StringObjectJsonTypeHandler());
        registry.register(Integer[].class, new IntegerArrayJsonTypeHandler());
        registry.register(Long[].class, new LongObjectJsonTypeHandler());
    }

    /**
     * 配置审计功能
     */
    @PostConstruct
    public void configureAudit() {
        // 开启审计功能
        AuditManager.setAuditEnable(true);
        // 设置 SQL 审计收集器
        MessageCollector collector = new ConsoleMessageCollector();
        AuditManager.setMessageCollector(collector);
    }
}

