package io.github.lazzz.sagittarius.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.Cache;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.redisson.annotation.Lock;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.system.cache.RouteCacheService;
import io.github.lazzz.sagittarius.system.enums.MenuTypeEnum;
import io.github.lazzz.sagittarius.system.model.entity.*;
import io.github.lazzz.sagittarius.system.model.request.form.SysMenuForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysMenuQuery;
import io.github.lazzz.sagittarius.system.model.vo.RouteVO;
import io.github.lazzz.sagittarius.system.model.vo.SysMenuVO;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.system.service.ISysMenuService;
import io.github.lazzz.sagittarius.system.mapper.SysMenuMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统菜单表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final Converter converter;

    private final Cache<String, List<RouteVO>> menuCache;

    private final RouteCacheService routeCacheService;

    @Qualifier("readWriteLockService")
    private final ObjectProvider<LockService> lockServiceProvider;

    @Override
    public List<SysMenuVO> listMenuVO(SysMenuQuery query) {
        List<SysMenu> menus = this.list(new QueryWrapper()
                .like(SysMenu::getName, query.getName(), StrUtil.isNotBlank(query.getName()))
                .orderBy(SysMenu::getSortOrder, true));
        Set<Long> parentIds = menus.stream()
                .map(SysMenu::getParentId)
                .collect(Collectors.toSet());
        Set<Long> menuIds = menus.stream()
                .map(SysMenu::getId)
                .collect(Collectors.toSet());
        // 获取根节点ID
        List<Long> rootIds = parentIds.stream()
                .filter(parentId -> !menuIds.contains(parentId))
                .toList();
        return rootIds.stream()
                .flatMap(rootId -> buildMenuTree(rootId, menus).stream())
                .toList();
    }

    @Override
    @Lock(name = "LOCK:MENU:${T(io.github.lazzz.sagittarius.common.utils.TenantContext).getTenantId()}:UPDATE",
            lockType = LockType.WRITE)
    public Boolean saveOrUpdateMenu(SysMenuForm form) {
        String path = form.getPath();
        Long parentId = form.getParentId();
        MenuTypeEnum menuType = form.getMenuType();
        If.ifThen(menuType == MenuTypeEnum.CATALOG, () -> {
            // 父级Id为0且路径不是以/开头时，添加/
            If.ifThen(parentId == 0 && !path.startsWith("/"), () -> form.setPath("/" + path));
            form.setComponent("Layout");
        }).elseIf(menuType == MenuTypeEnum.EXTLINK, () -> form.setComponent(null));
        SysMenu menu = converter.convert(form, SysMenu.class);
        String treePath = generateMenuTreePath(parentId);
        menu.setTreePath(treePath);
        var rs = this.saveOrUpdate(menu);
        routeCacheService.refreshMenuCache();
        return rs;
    }

    /**
     * 菜单构建路径生成
     *
     * @param parentId 父ID
     * @return 父节点路径以英文逗号(, )分割，eg: 1,2,3
     */
    private String generateMenuTreePath(Long parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            return String.valueOf(parentId);
        } else {
            SysMenu parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }

    @Override
    @Lock(name = "LOCK:MENU:${T(io.github.lazzz.sagittarius.common.utils.TenantContext).getTenantId()}:DELETE",
            lockType = LockType.WRITE)
    public Boolean deleteMenu(Long menuId) {
        var rs = this.remove(QueryWrapper.create()
                .from(SysMenu.class)
                .eq(SysMenu::getId, menuId)
                .or("CONCAT (',',tree_path,',') LIKE CONCAT('%,',?,',%')", menuId)
        );
        routeCacheService.refreshMenuCache();
        return rs;
    }

    /**
     * 构建菜单树
     *
     * @param parentId 父级ID
     * @param menus    菜单集合
     * @return {@link List<SysMenuVO>}
     */
    private List<SysMenuVO> buildMenuTree(Long parentId, List<SysMenu> menus) {
        return CollectionUtil.emptyIfNull(menus)
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(entity -> {
                    SysMenuVO vo = converter.convert(entity, SysMenuVO.class);
                    List<SysMenuVO> children = buildMenuTree(entity.getId(), menus);
                    vo.setChildren(children);
                    return vo;
                }).toList();
    }

    @Override
    public List<RouteVO> listRoutes() {
        String cacheKey = CacheConstants.MENU_PREFIX + "route";
        List<RouteVO> rs = menuCache.get(cacheKey);
        if (rs != null) {
            System.out.println("直接返回");
            return rs;
        }
        rs = routeCacheService.getFromCache();
        if (rs != null) {
            System.out.println("读锁内查询（已缓存）");
            return rs;
        }
        // 缓存为空：释放读锁后，调用带写锁的方法
        // 这里会自动释放当前读锁（由切面控制）
        return routeCacheService.refreshMenuCache();
    }

}