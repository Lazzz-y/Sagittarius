package io.github.lazzz.sagittarius.user.service.impl;


import com.mybatisflex.core.query.QueryChain;
import io.github.lazzz.user.dto.UserAuthDTO;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.user.service.SysUserService;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import io.github.lazzz.sagittarius.user.mapper.SysUserMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 用户表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public UserAuthDTO getUserAuthDTO(String username) {
       return QueryChain.of(this.getMapper()).from(SysUser.class)
                .where(SysUser::getUsername).eq(username)
               .oneAs(UserAuthDTO.class);

    }
}