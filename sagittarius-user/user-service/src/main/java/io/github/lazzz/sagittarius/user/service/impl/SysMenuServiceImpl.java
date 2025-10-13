package io.github.lazzz.sagittarius.user.service.impl;


import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.ISysMenuService;
import io.github.lazzz.sagittarius.user.model.entity.SysMenu;
import io.github.lazzz.sagittarius.user.mapper.SysMenuMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 系统菜单表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

}