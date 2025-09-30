package io.github.lazzz.sagittarius.common.listener;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.SetListener;
import com.mybatisflex.annotation.UpdateListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;

/**
 * TODO
 * 
 * @author Lazzz 
 * @date 2025/09/30 14:53
**/
public abstract class BaseListener {

    protected Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> map = null;
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            map = jwtAuthenticationToken.getTokenAttributes();
        }
        Assert.isTrue(CollectionUtil.isNotEmpty(map), "无法从上下文获取用户数据");
        return Convert.toLong(map.get("userId"));
    }

}

