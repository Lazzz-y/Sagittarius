package io.github.lazzz.sagittarius.user.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.common.utils.condition.IfFlattener;
import io.github.lazzz.sagittarius.user.model.entity.SysMenuRelation;
import io.github.lazzz.sagittarius.user.model.request.form.SysPermForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysPermPageQuery;
import io.github.lazzz.sagittarius.user.model.vo.SysPermVO;
import io.github.lazzz.sagittarius.user.service.ISysMenuRelationService;
import io.github.lazzz.sagittarius.user.service.ISysRolePermissionService;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysPermissionService;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import io.github.lazzz.sagittarius.user.mapper.SysPermissionMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 系统权限表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    private final Converter converter;

    private final ISysRolePermissionService sysRolePermissionService;

    private final ISysMenuRelationService sysMenuRelationService;

    @Override
    @Transactional
    public boolean saveOrUpdatePerm(SysPermForm form) {
        Long id = form.getId();
        SysPermission oldPerm = null;
        if (id != null) {
            oldPerm = this.getById(id);
            Assert.isTrue(oldPerm != null, "权限不存在！");
        }
        String permCode = form.getPermCode();
        long count = this.count(
                queryChain().ne(SysPermission::getId, id, id != null)
                        .and(wrapper -> {
                            wrapper.eq(SysPermission::getPermCode, permCode)
                                    .or(SysPermission::getPermName).eq(form.getPermName());
                        })
        );
        Assert.isTrue(count == 0, "权限名称或权限编码已存在，请修改后重试！");
        SysPermission perm = converter.convert(form, SysPermission.class);
        var rs = this.saveOrUpdate(perm);
        if (rs) {
            if (oldPerm != null && !StrUtil.equals(oldPerm.getPermCode(), perm.getPermCode())) {
                // TODO 后续考虑做 redis 权限缓存刷新
            }
        }
        return rs;
    }

    @Override
    @Transactional
    public boolean deletePerms(String ids) {
        List<Long> permIds = Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
        for (Long id : permIds) {
            SysPermission perm = this.getById(id);
            Assert.isTrue(perm != null, "权限不存在");

            // 判断权限是否被角色使用
            Assert.isTrue(
                    !sysRolePermissionService.hasAssignedRolesToPerms(id)
                    , "权限[{}]被角色使用，请先解除关联后删除"
                    , perm.getPermName()
            );
            // 判断权限是否被菜单使用
            Assert.isTrue(
                    !sysMenuRelationService.hasAssignedMenusToPerms(id)
                    , "权限[{}]被菜单使用，请先解除关联后删除"
                    , perm.getPermName()
            );
            var rs = this.removeById(id);
            if (rs) {
                // TODO 刷新权限缓存
            }
        }
        return true;
    }

    @Override
    public Page<SysPermVO> getPermsPage(SysPermPageQuery query) {
        var wrapper = new QueryWrapper();
        If.withNotBlank(query.getPermCode(),
                str -> wrapper.where(SysPermission::getPermCode).like(str));
        If.withNotBlank(query.getPermName(),
                str -> wrapper.where(SysPermission::getPermName).like(str));
        If.withNotBlank(query.getResourceType(),
                str -> wrapper.where(SysPermission::getResourceType).eq(str));
        If.withNotBlank(query.getResourceId(),
                str -> wrapper.where(SysPermission::getResourceId).eq(str));
        Page<SysPermission> page = this.page(query.toPage(), wrapper);
        return page.map(m -> converter.convert(m, SysPermVO.class));
    }

    @Override
    @Transactional
    public boolean assignMenuToPerm(Long permId, List<Long> menuIds) {
        SysPermission perm = this.getById(permId);
        Assert.isTrue(perm != null, "权限不存在");

        // 删除菜单权限关系
        sysMenuRelationService.remove(QueryChain
                .of(SysMenuRelation.class)
                .from(SysMenuRelation.class)
                .eq(SysMenuRelation::getPermissionId, permId)
        );

        // 新增菜单权限关系
        IfFlattener.of(menuIds).whenNotEmpty(ids -> {
            sysMenuRelationService.saveBatch(ids
                    .stream()
                    .map(menuId -> SysMenuRelation.builder()
                            .menuId(menuId)
                            .permissionId(permId)
                            .build())
                    .toList()
            );
        });

        // TODO 刷新权限缓存

        return true;
    }
}