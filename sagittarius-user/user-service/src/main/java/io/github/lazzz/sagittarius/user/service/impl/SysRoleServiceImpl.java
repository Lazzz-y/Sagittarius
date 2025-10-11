package io.github.lazzz.sagittarius.user.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.sagittarius.user.model.request.form.SysRoleForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysRolePageQuery;
import io.github.lazzz.sagittarius.user.model.vo.SysRoleVO;
import io.github.lazzz.sagittarius.user.service.ISysRolePermissionService;
import io.github.lazzz.sagittarius.user.service.ISysUserRoleService;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysRoleService;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.lazzz.sagittarius.user.mapper.SysRoleMapper;
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
            if (oldRole != null && !StrUtil.equals(oldRole.getRoleCode(), role.getRoleCode())){
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
        for (Long id : roleIds){
            SysRole role = this.getById(id);
            Assert.isTrue(role != null, "角色不存在");

            // 判断角色是否被用户关联
            boolean isRoleAssigned = sysUserRoleService.hasAssignedUsers(id);
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
        return null;
    }

    @Override
    public boolean assignPermToRole(Long roleId, List<Long> menusIds) {
        return false;
    }
}