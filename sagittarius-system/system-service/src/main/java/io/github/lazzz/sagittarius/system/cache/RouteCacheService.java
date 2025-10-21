package io.github.lazzz.sagittarius.system.cache;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.Cache;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.enums.StatusEnum;
import io.github.lazzz.sagittarius.common.redisson.annotation.Lock;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.system.enums.MenuTypeEnum;
import io.github.lazzz.sagittarius.system.mapper.SysMenuMapper;
import io.github.lazzz.sagittarius.system.model.bo.RouteBO;
import io.github.lazzz.sagittarius.system.model.vo.RouteVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由菜单缓存服务
 * 
 * @author Lazzz 
 * @date 2025/10/20 21:36
**/
@Service
@RequiredArgsConstructor
public class RouteCacheService {

    private final SysMenuMapper sysMenuMapper;

    private final Cache<String, List<RouteVO>> menuCache;

    @Lock(name = "LOCK:MENU:#{T(io.github.lazzz.sagittarius.common.utils.TenantContext).getTenantId()}:ROUTE",
            lockType = LockType.READ)
    public List<RouteVO> getFromCache() {
        return menuCache.get(CacheConstants.MENU_PREFIX + "route");
    }

    @Lock(name = "LOCK:MENU:#{T(io.github.lazzz.sagittarius.common.utils.TenantContext).getTenantId()}:ROUTE",
            lockType = LockType.WRITE)
    public List<RouteVO> refreshMenuCache() {
        String cacheKey = CacheConstants.MENU_PREFIX + "route";
        List<RouteVO> rs = menuCache.get(cacheKey);
        if (rs != null) {
            return rs;
        }
        // 确实无缓存，查询数据库并更新
        rs = buildRoutes(SystemConstants.ROOT_NODE_ID, sysMenuMapper.listRoutes());
        menuCache.put(cacheKey, rs);
        return rs;
    }

    public void clearCache() {
        menuCache.remove(CacheConstants.MENU_PREFIX + "route");
    }

    private List<RouteVO> buildRoutes(Long parentId, List<RouteBO> menus) {
        List<RouteVO> routes = new ArrayList<>();

        for (RouteBO menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                RouteVO vo = boToVO(menu);
                List<RouteVO> children = buildRoutes(menu.getId(), menus);
                if (!children.isEmpty()) {
                    vo.setChildren(children);
                }
                routes.add(vo);
            }
        }
        return routes;
    }

    private RouteVO boToVO(RouteBO bo) {
        RouteVO vo = new RouteVO();
        // 路由name 转换为大驼峰
        String routeName = StringUtils.capitalize(StrUtil.toCamelCase(bo.getPath(), '-'));
        // 根据 name 路由跳转 this.$router.push({ name:xxx })
        vo.setName(routeName);
        // 根据path路由跳转 this.$router.push({path:xxx})
        vo.setPath(bo.getPath());
        vo.setRedirect(bo.getRedirect());
        vo.setComponent(bo.getComponent());

        RouteVO.Meta meta = new RouteVO.Meta();
        meta.setTitle(bo.getName());
        meta.setIcon(bo.getIcon());
        meta.setRoles(bo.getRoles());
        meta.setHidden(StatusEnum.DISABLE.getValue().equals(bo.getIsVisible()));
        // 菜单类型为[菜单]时，设置缓存
        If.ifThen(
                MenuTypeEnum.MENU.equals(bo.getMenuType()) &&
                        ObjectUtil.equals(bo.getKeepAlive(), 1),
                () -> meta.setKeepAlive(true)
        );

        // 菜单类型为[目录]时，设置[目录]是否始终显示
        If.ifThen(
                MenuTypeEnum.CATALOG.equals(bo.getMenuType()) &&
                        ObjectUtil.equals(bo.getAlwaysShow(), 1),
                () -> meta.setAlwaysShow(true)
        );
        vo.setMeta(meta);
        return vo;
    }

}

