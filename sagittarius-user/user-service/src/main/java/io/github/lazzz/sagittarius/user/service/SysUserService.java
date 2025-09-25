package io.github.lazzz.sagittarius.user.service;


import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.user.dto.UserAuthDTO;

/**
 * 用户表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface SysUserService extends IService<SysUser> {

    UserAuthDTO getUserAuthDTO(String username);

}