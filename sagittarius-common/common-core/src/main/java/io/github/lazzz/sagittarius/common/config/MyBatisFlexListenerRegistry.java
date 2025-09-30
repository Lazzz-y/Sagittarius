package io.github.lazzz.sagittarius.common.config;


import com.mybatisflex.core.FlexGlobalConfig;
import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
import io.github.lazzz.sagittarius.common.listener.BaseEntityInsertListener;
import io.github.lazzz.sagittarius.common.listener.BaseEntityUpdateListener;
import org.springframework.context.annotation.Configuration;

/**
 * 全局 MyBatisFlex 监听器注册
 * 
 * @author Lazzz 
 * @date 2025/09/30 14:39
**/
@Configuration
public class MyBatisFlexListenerRegistry {

    public MyBatisFlexListenerRegistry() {

        BaseEntityInsertListener insertListener = new BaseEntityInsertListener();
        BaseEntityUpdateListener updateListener = new BaseEntityUpdateListener();

        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();

        // 设置 BaseEntity 类注册监听器
        config.registerInsertListener(insertListener, BaseSnowflakeEntity.class);
        config.registerUpdateListener(updateListener, BaseSnowflakeEntity.class);

    }

}

