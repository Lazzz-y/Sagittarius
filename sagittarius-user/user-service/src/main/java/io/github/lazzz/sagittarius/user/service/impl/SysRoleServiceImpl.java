package io.github.lazzz.sagittarius.user.service.impl;


import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysRoleService;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.lazzz.sagittarius.user.mapper.SysRoleMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

}