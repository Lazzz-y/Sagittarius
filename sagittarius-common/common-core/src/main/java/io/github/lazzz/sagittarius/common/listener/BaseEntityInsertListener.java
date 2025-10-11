package io.github.lazzz.sagittarius.common.listener;


import com.mybatisflex.annotation.InsertListener;
import io.github.lazzz.sagittarius.common.base.BaseEntity;
import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
import io.github.lazzz.sagittarius.common.base.component.Deleted;
import io.github.lazzz.sagittarius.common.utils.condition.If;

import java.time.LocalDateTime;

/**
 * 监听实体插入事件
 * 
 * @author Lazzz 
 * @date 2025/09/30 14:24
**/
public class BaseEntityInsertListener extends BaseListener implements InsertListener {

    @Override
    public void onInsert(Object entity) {
        BaseEntity base = (BaseEntity) entity;
        var userId = getUserId();
        var now = LocalDateTime.now();
        base.setCreateBy(userId);
        base.setUpdateBy(userId);

        base.setCreateTime(now);
        base.setUpdateTime(now);
        if (base instanceof Deleted other) {
            other.setDeleted(0);
        }
    }
}

