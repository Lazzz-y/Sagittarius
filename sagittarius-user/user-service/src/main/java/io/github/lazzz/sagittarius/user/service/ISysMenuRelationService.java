package io.github.lazzz.sagittarius.user.service;


import io.github.lazzz.sagittarius.user.model.entity.SysMenuRelation;
import com.mybatisflex.core.service.IService;

/**
 * 菜单权限关联表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysMenuRelationService extends IService<SysMenuRelation> {

    /**
     * 判断菜单是否被权限关联
     *
     * @param permId 权限ID
     * @return {@link Boolean}
     */
    boolean hasAssignedMenusToPerms(Long permId);

}