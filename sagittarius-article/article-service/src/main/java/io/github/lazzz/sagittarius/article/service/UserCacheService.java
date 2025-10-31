package io.github.lazzz.sagittarius.article.service;


import com.alicp.jetcache.Cache;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.redisson.annotation.Lock;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;
import io.github.lazzz.sagittarius.system.api.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 用户缓存服务
 * 
 * @author Lazzz 
 * @date 2025/10/31 19:16
**/
@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final Cache<String, String> userCache;

    private final UserFeignClient userFeignClient;

    public String getUserNameById(Long userId) {
        String key = CacheConstants.SUB_USER_PREFIX + userId;
        String username = userCache.get(key);
        if (StringUtils.isNotBlank(username)) {
            return username;
        }
        username = getUserNameFormCache(userId);
        if (StringUtils.isNotBlank(username)) {
            return username;
        }
        return refreshUserNameToCache(userId);
    }

    /**
     * 根据用户id获取用户名（从缓存中）
     *
     * @author Lazzz
     * @date 2025/10/31
     *
     * @param userId 用户id
     * @return {@link String}
     */
    @Lock(
            name = CacheConstants.SPEL_LOCK_ROUTE_KEY + "#{userId}",
            lockType = LockType.READ
    )
    public String getUserNameFormCache(Long userId) {
        String key = CacheConstants.SUB_USER_PREFIX + userId;
        return userCache.get(key);
    }

    @Lock(
            name = CacheConstants.SPEL_LOCK_ROUTE_KEY + "#{userId}",
            lockType = LockType.WRITE
    )
     public String refreshUserNameToCache(Long userId) {
        String key = CacheConstants.SUB_USER_PREFIX + userId;
        String username = userCache.get(key);
        if (StringUtils.isNotBlank(username)) {
            // 写锁内二次查询
            return username;
        }
        username = userFeignClient.getUserInfoDTO(userId).getUsername();
        if (StringUtils.isNotBlank(username)) {
            userCache.put(key, username);
        }
        return username;
    }

}

