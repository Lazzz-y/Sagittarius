package io.github.lazzz.sagittarius.user.service.impl;


import io.github.lazzz.sagittarius.user.model.entity.SysMenu;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysMenuRelationService;
import io.github.lazzz.sagittarius.user.model.entity.SysMenuRelation;
import io.github.lazzz.sagittarius.user.mapper.SysMenuRelationMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 菜单权限关联表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysMenuRelationServiceImpl extends ServiceImpl<SysMenuRelationMapper, SysMenuRelation> implements ISysMenuRelationService {

    @Override
    public boolean hasAssignedMenusToPerms(Long permId) {
        return this.count(queryChain()
                .from(SysMenuRelation.class)
                .innerJoin(SysPermission.class)
                .on(SysPermission::getId, SysMenuRelation::getPermissionId)
                .innerJoin(SysMenu.class)
                .on(SysMenu::getId, SysMenuRelation::getMenuId)
                .eq(SysPermission::getId, permId)) > 0;
    }
}