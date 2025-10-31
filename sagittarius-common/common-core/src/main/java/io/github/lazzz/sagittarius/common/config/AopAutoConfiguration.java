package io.github.lazzz.sagittarius.common.config;

import io.github.lazzz.sagittarius.common.aspect.LogAspect;
import io.github.lazzz.sagittarius.common.aspect.TenantContextAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置类
 *
 * @author Lazzz
 * @date 2025/10/11 20:03
 **/
@Configuration
@Import({LogAspect.class, TenantContextAspect.class})  // 导入切面类
public class AopAutoConfiguration {
    // 自动配置类，无需额外代码
}