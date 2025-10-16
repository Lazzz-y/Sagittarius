package io.github.lazzz.sagittarius.system.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.common.security.util.SecurityUtils;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.system.model.bo.SysUserProfileBO;
import io.github.lazzz.sagittarius.system.model.entity.SysRole;
import io.github.lazzz.sagittarius.system.model.entity.SysUserRole;
import io.github.lazzz.sagittarius.system.model.request.form.SysUserSaveForm;
import io.github.lazzz.sagittarius.system.model.request.form.SysUserUpdateForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysUserPageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysUserProfileVO;
import io.github.lazzz.sagittarius.system.model.vo.SysUserVO;
import io.github.lazzz.sagittarius.system.service.ISysRolePermissionService;
import io.github.lazzz.sagittarius.system.service.ISysUserRoleService;
import io.github.lazzz.sagittarius.system.dto.UserAuthDTO;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.system.service.ISysUserService;
import io.github.lazzz.sagittarius.system.model.entity.SysUser;
import io.github.lazzz.sagittarius.system.mapper.SysUserMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 用户表 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final Converter converter;

    private final PasswordEncoder passwordEncoder;

    private final StringRedisTemplate redisTemplate;

    private final ISysUserRoleService sysUserRoleService;

    private final ISysRolePermissionService sysRolePermissionService;

    @Override
    public UserAuthDTO getUserAuthDTO(String username) {
        sysRolePermissionService.lazyLoadRole();
        return queryChain()
                .select("u.id", "username", "nickname", "password", "email",
                        "sex", "phone", "avatar", "status", "user_type", "u.tenant_id")
                .select("role_code as roles")
                .from(SysUser.class).as("u")
                .innerJoin(SysUserRole.class)
                .on(SysUserRole::getUserId, SysUser::getId)
                .innerJoin(SysRole.class)
                .on(SysRole::getId, SysUserRole::getRoleId)
                .where(SysUser::getUsername).eq(username)
                .oneAs(UserAuthDTO.class);
    }

    /**
     * 登出
     *
     * @return java.lang.Boolean
     * @author Lazzz
     * @date 2025/9/29
     */
    @Override
    public Boolean logout() {
        String jti = SecurityUtils.getJti();
        // 使用 Optional 处理可能的 null
        Optional<Long> expireTimeOpt = Optional.ofNullable(SecurityUtils.getExp());
        // 当前时间 秒
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        expireTimeOpt.ifPresent(expireTime -> {
            expireTime = expireTime / 1000;
            if (expireTime> currentTimeSeconds) {
                // token 未过期，添加至缓存作为黑名单，缓存时间为 token 剩余的有效时间
                long remainingTimeInSeconds = expireTime - currentTimeSeconds;
                redisTemplate.opsForValue().set(CacheConstants.TOKEN_BLACKLIST_PREFIX + jti, "", remainingTimeInSeconds, TimeUnit.SECONDS);
            }
        });
        if (expireTimeOpt.isEmpty()) {
            // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(CacheConstants.TOKEN_BLACKLIST_PREFIX + jti, "");
        }
        return true;
    }

    /**
     * 修改密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return java.lang.Boolean
     * @author Lazzz
     * @date 2025/9/29
     */
    @Override
    @Transactional
    public Boolean updatePassword(Long id, String password) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        return updateById(user);
    }

    @Override
    @Transactional
    public Boolean saveUser(SysUserSaveForm form) {
        form.init();
        Assert.isTrue(this.mapper.selectCountByQuery(
                queryChain()
                        .where(SysUser::getUsername)
                        .eq(form.getUsername())) == 0, "用户名已存在");

        If.ifThen(StrUtil.isNotBlank(form.getPhone()), () -> {
            Assert.isTrue(this.mapper.selectCountByQuery(
                    queryChain()
                            .where(SysUser::getPhone)
                            .eq(form.getPhone())) == 0, "手机号已存在");
        }).elseIf(StrUtil.isNotBlank(form.getEmail()), () -> {
            Assert.isTrue(this.mapper.selectCountByQuery(
                    queryChain()
                            .where(SysUser::getEmail)
                            .eq(form.getEmail())) == 0, "邮箱已存在");
        });
        // 密码加密
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        var user = converter.convert(form, SysUser.class);
        boolean userResult = this.save(user);
        boolean roleResult = this.sysUserRoleService.assignRoleToUser(user.getId(), List.of(SystemConstants.DEFAULT_USER_ROLE_ID));
        return userResult && roleResult;
    }

    @Override
    @Transactional
    public Boolean updateUser(Long userId, SysUserUpdateForm form) {
        Assert.isTrue(this.mapper.selectCountByQuery(
                queryChain()
                        .eq(SysUser::getUsername, form.getUsername())
                        .ne(SysUser::getId, userId)) == 0, "用户名已存在");
        var user = converter.convert(form, SysUser.class);
        user.setId(userId);
        return this.updateById(user);
    }

    @Override
    public SysUserVO getUserInfo(Serializable id) {
        return converter.convert(this.mapper.selectOneById(id), SysUserVO.class);
    }

    @Override
    public List<SysUserVO> getUserList() {
        return this.mapper.selectAll().stream().map(m -> converter.convert(m, SysUserVO.class)).toList();
    }

    @Override
    public Page<SysUserVO> getUserPage(SysUserPageQuery query) {
        QueryWrapper wrapper = QueryWrapper.create().from(SysUser.class);

        If.ifThen(StrUtil.isNotBlank(query.getUsername()), () -> wrapper.where(SysUser::getUsername).like(query.getUsername()));
        If.ifThen(StrUtil.isNotBlank(query.getNickname()), () -> wrapper.where(SysUser::getNickname).like(query.getNickname()));
        If.ifThen(StrUtil.isNotBlank(query.getPhone()), () -> wrapper.where(SysUser::getPhone).like(query.getPhone()));
        If.ifThen(StrUtil.isNotBlank(query.getEmail()), () -> wrapper.where(SysUser::getEmail).like(query.getEmail()));
        If.ifThen(query.getStatus() != null, () -> wrapper.where(SysUser::getStatus).eq(query.getStatus()));

        Page<SysUser> page = this.mapper.paginate(query.toPage(), wrapper);
        return page.map(m -> converter.convert(m, SysUserVO.class));
    }

    /**
     * 获取用户信息
     *
     * @return {@link SysUserProfileVO}
     * @author Lazzz
     * @date 2025/9/29
     */
    @Override
    public SysUserProfileVO getUserProfile() {
        Long userId = SecurityUtils.getUserId();
        // 获取用户个人中心信息
        SysUserProfileBO sysUserProfileBO = queryChain()
                .select("id", "username", "nickname", "sex", "email", "phone",
                        "avatar", "user_type", "bio", "website", "article_count",
                        "comment_count", "view_count", "create_at")
                .from(SysUser.class).as("u")
                .where(SysUser::getId).eq(userId)
                .oneAs(SysUserProfileBO.class);
        return converter.convert(sysUserProfileBO, SysUserProfileVO.class);
    }
}