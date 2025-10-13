package io.github.lazzz.sagittarius.user.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.common.security.service.PermissionService;
import io.github.lazzz.sagittarius.common.constant.RedisConstants;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.enums.StatusEnum;
import io.github.lazzz.sagittarius.common.redis.annotation.CacheKeySuffix;
import io.github.lazzz.sagittarius.common.utils.TenantContext;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.user.enums.MenuTypeEnum;
import io.github.lazzz.sagittarius.user.model.bo.RouteBO;
import io.github.lazzz.sagittarius.user.model.entity.*;
import io.github.lazzz.sagittarius.user.model.request.form.SysMenuForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysMenuQuery;
import io.github.lazzz.sagittarius.user.model.vo.RouteVO;
import io.github.lazzz.sagittarius.user.model.vo.SysMenuVO;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysMenuService;
import io.github.lazzz.sagittarius.user.mapper.SysMenuMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统菜单表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final Converter converter;

    private final SysMenuMapper sysMenuMapper;

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
    public boolean saveOrUpdateMenu(SysMenuForm form) {
        String path = form.getPath();
        Long parentId = form.getParentId();
        MenuTypeEnum menuType = form.getMenuType();
        If.ifThen(menuType == MenuTypeEnum.CATALOG, () -> {
            // 父级Id为0且路径不是以/开头时，添加/
            If.ifThen(parentId == 0 && !path.startsWith("/"), () -> form.setPath("/" + path));
            form.setComponent("Layout");
        }).elseIf(menuType == MenuTypeEnum.EXTLINK, () -> form.setComponent(null));
        SysMenu menu = converter.convert(form, SysMenu.class);
        var rs = this.saveOrUpdate(menu);
        If.ifThen(rs && form.getId() != null, () -> {
            // TODO 刷新缓存
        });
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
    @CacheKeySuffix("route")
    @Cacheable(cacheNames = "menus", keyGenerator = "tenantKeyGenerator")
    public List<RouteVO> listRoutes() {
        var bos = sysMenuMapper.listRoutes();
        return buildRoutes(SystemConstants.ROOT_NODE_ID, bos);
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