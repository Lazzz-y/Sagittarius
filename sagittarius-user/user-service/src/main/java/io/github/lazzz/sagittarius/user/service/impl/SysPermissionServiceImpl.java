package io.github.lazzz.sagittarius.user.service.impl;


import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysPermissionService;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import io.github.lazzz.sagittarius.user.mapper.SysPermissionMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 系统权限表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

}