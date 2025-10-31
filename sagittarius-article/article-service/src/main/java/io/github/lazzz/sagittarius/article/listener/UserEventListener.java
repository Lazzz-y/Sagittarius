package io.github.lazzz.sagittarius.article.listener;


import com.alicp.jetcache.Cache;
import io.github.lazzz.common.rabbitmq.constant.MQConstants;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.event.user.UserInfoEvent;
import io.github.lazzz.sagittarius.common.redisson.annotation.Lock;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 用户事件监听器
 *
 * @author Lazzz
 * @date 2025/10/25 21:44
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final Cache<String, String> userCache;

    @RabbitListener(queues = MQConstants.QUEUE_USER_INFO)
    @Lock(
            name = CacheConstants.SPEL_LOCK_USER_EVENT_KEY + "#{event.id}",
            lockType = LockType.WRITE
    )
    public void handleUserUpdate(UserInfoEvent event){
        try {
            TenantContext.setTenantId(event.getTenantId());
            String key = CacheConstants.SUB_USER_PREFIX + event.getId();
            userCache.put(key, event.getNickname());
        } finally {
            TenantContext.clear();
        }
    }

}

