package io.github.lazzz.sagittarius.common.listener;


import com.mybatisflex.annotation.UpdateListener;
import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;

import java.time.LocalDateTime;

/**
 * 监听实体更新事件
 * 
 * @author Lazzz 
 * @date 2025/09/30 14:44
**/
public class BaseEntityUpdateListener extends BaseListener implements UpdateListener {
    @Override
    public void onUpdate(Object entity) {
        BaseSnowflakeEntity base = (BaseSnowflakeEntity) entity;
        var userId = getUserId();
        var now = LocalDateTime.now();

        base.setUpdateBy(userId);
        base.setUpdateTime(now);
    }
}

