package io.github.lazzz.sagittarius.system.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.common.utils.condition.IfFlattener;
import io.github.lazzz.sagittarius.system.model.entity.SysRolePermission;
import io.github.lazzz.sagittarius.system.model.request.form.SysRoleForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysRolePageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysRoleVO;
import io.github.lazzz.sagittarius.system.service.ISysRolePermissionService;
import io.github.lazzz.sagittarius.system.service.ISysUserRoleService;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.system.service.ISysRoleService;
import io.github.lazzz.sagittarius.system.model.entity.SysRole;
import io.github.lazzz.sagittarius.system.mapper.SysRoleMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final Converter converter;

    private final ISysRolePermissionService sysRolePermissionService;

    private final ISysUserRoleService sysUserRoleService;

    @Override
    @Transactional
    public boolean saveOrUpdateRole(SysRoleForm form) {
        Long id = form.getId();
        SysRole oldRole = null;
        if (id != null) {
            oldRole = this.getById(id);
            Assert.isTrue(oldRole != null, "角色不存在");
        }
        String roleCode = form.getRoleCode();
        long count = this.count(
                queryChain().ne(SysRole::getId, id, id != null)
                        .and(wrapper -> {
                            wrapper.eq(SysRole::getRoleCode, roleCode)
                                    .or(SysRole::getRoleName).eq(form.getRoleName());
                        })
        );
        Assert.isTrue(count == 0, "角色名称或角色编码已存在，请修改后重试！");
        SysRole role = converter.convert(form, SysRole.class);
        var rs = this.saveOrUpdate(role);
        if (rs) {
            // 判断是否修改了角色编码，如果修改了则刷新角色权限缓存
            if (oldRole != null && !StrUtil.equals(oldRole.getRoleCode(), role.getRoleCode())) {
                sysRolePermissionService.refreshRolePermsCache(oldRole.getRoleCode(), roleCode);
            }
        }
        sysRolePermissionService.refreshRolePermsCache(role.getRoleCode());
        return rs;
    }

    @Override
    @Transactional
    public boolean deleteRoles(String ids) {
        List<Long> roleIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong).toList();
        for (Long id : roleIds) {
            SysRole role = this.getById(id);
            Assert.isTrue(role != null, "角色不存在");

            // 判断角色是否被用户关联
            boolean isRoleAssigned = sysUserRoleService.hasAssignedUsersToRoles(id);
            Assert.isTrue(!isRoleAssigned, "角色【{}】已分配用户，请先解除关联后删除", role.getRoleName());
            boolean deleteResult = this.removeById(id);
            if (deleteResult) {
                sysRolePermissionService.refreshRolePermsCache(role.getRoleCode());
            }
        }
        return true;
    }

    @Override
    public Page<SysRoleVO> getRolePage(SysRolePageQuery query) {
        QueryWrapper wrapper = QueryWrapper.create().from(SysRole.class);
        If.withNotBlank(query.getRoleCode(), str -> wrapper.where(SysRole::getRoleCode).like(str));
        If.withNotBlank(query.getRoleName(), str -> wrapper.where(SysRole::getRoleName).like(str));
        Page<SysRole> page = this.mapper.paginate(query.toPage(), wrapper);
        return page.map(m -> converter.convert(m, SysRoleVO.class));
    }

    @Override
    @Transactional
    public boolean assignPermToRole(Long roleId, List<Long> permIds) {
        SysRole role = this.getById(roleId);
        Assert.isTrue(role != null, "角色不存在");

        // 删除角色权限关系
        sysRolePermissionService.remove(QueryChain
                .of(SysRolePermission.class)
                .from(SysRolePermission.class)
                .eq(SysRolePermission::getRoleId, roleId)
        );

        // 新增角色权限关系
        IfFlattener.of(permIds).whenNotEmpty(ids -> {
            List<SysRolePermission> rps = ids.stream()
                    .map(permId -> new SysRolePermission(roleId, permId))
                    .toList();
            sysRolePermissionService.saveBatch(rps);
        });

        // 刷新角色权限缓存
        sysRolePermissionService.refreshRolePermsCache(role.getRoleCode());

        return true;
    }
}