package io.github.lazzz.sagittarius.user.service;


import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.user.model.request.form.SysUserSaveForm;
import io.github.lazzz.sagittarius.user.model.request.form.SysUserUpdateForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysUserPageQuery;
import io.github.lazzz.sagittarius.user.model.vo.SysUserProfileVO;
import io.github.lazzz.sagittarius.user.model.vo.SysUserVO;
import io.github.lazzz.user.dto.UserAuthDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 用户表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */

public interface ISysUserService extends IService<SysUser> {

    /**
     * 获取用户认证信息
     *
     * @author Lazzz
     * @date 2025/9/27
     * @param username 用户名
     * @return {@link UserAuthDTO}
     */
    UserAuthDTO getUserAuthDTO(String username);

    /**
     * 登出
     *
     * @author Lazzz
     * @date 2025/9/29
     * @return {@link Boolean}
     */
    Boolean logout();

    /**
     * 修改密码
     *
     * @author Lazzz
     * @date 2025/9/29
     * @return {@link Boolean}
     */
    Boolean updatePassword(Long id, String password);

    /**
     * 增加用户
     * @param form 用户表单
     * @return {@link Boolean}
     */
    Boolean saveUser(SysUserSaveForm form);

    /**
     * 修改用户
     * @param form 用户表单
     * @return {@link Boolean}
     */
    Boolean updateUser(Long userId, SysUserUpdateForm form);

    /**
     * 获取用户信息
     *
     * @author Lazzz
     * @date 2025/9/27
     * @param id 用户id
     * @return {@link SysUserVO}
     */
    SysUserVO getUserInfo(Serializable id);

    /**
     * 查询用户列表
     *
     * @author Lazzz
     * @date 2025/9/27
     * @return {@link List<SysUserVO>}
     */
    List<SysUserVO> getUserList();

    /**
     * 分页查询用户列表
     *
     * @author Lazzz
     * @date 2025/9/27
     * @param query 分页查询参数
     * @return {@link Page<SysUserVO>}
     */
    Page<SysUserVO> getUserPage(SysUserPageQuery query);

    /**
     * 获取用户个人资料
     *
     * @author Lazzz
     * @date 2025/9/27
     * @return io.github.lazzz.sagittarius.user.model.vo.SysUserProfileVO
     */
    SysUserProfileVO getUserProfile();

}