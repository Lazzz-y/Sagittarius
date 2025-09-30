package io.github.lazzz.sagittarius.user.service.impl;


import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysUserRoleService;
import io.github.lazzz.sagittarius.user.model.entity.SysUserRole;
import io.github.lazzz.sagittarius.user.mapper.SysUserRoleMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 用户角色关联表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {


    @Override
    public boolean defaultRole() {
        return false;
    }
}