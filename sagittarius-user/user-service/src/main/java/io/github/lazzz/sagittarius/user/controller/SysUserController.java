package io.github.lazzz.sagittarius.user.controller;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.user.model.request.form.SysUserSaveForm;
import io.github.lazzz.sagittarius.user.model.request.form.SysUserUpdateForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysUserPageQuery;
import io.github.lazzz.sagittarius.user.model.vo.SysUserProfileVO;
import io.github.lazzz.sagittarius.user.model.vo.SysUserVO;
import io.github.lazzz.sagittarius.user.service.ISysRolePermissionService;
import io.github.lazzz.user.dto.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.lazzz.sagittarius.user.service.ISysUserService;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "01.用户接口")
public class SysUserController {

    private final ISysUserService sysUserService;

    private final ISysRolePermissionService sysRolePermissionService;

    @Operation(summary = "获取用户认证信息", hidden = true)
    @RequestMapping("/{username}/auth")
    public Result<UserAuthDTO> getUserAuthDTO(
            @Parameter(description = "用户名") @PathVariable String username) {
        UserAuthDTO userAuthDTO = sysUserService.getUserAuthDTO(username);
        return Result.success(userAuthDTO);
    }

    @Operation(summary = "修改密码")
    @PatchMapping("/{id}/password")
    @PreAuthorize("@ss.hasHigherRole('USER')")
    public Result<Boolean> updatePassword(
            @PathVariable Long id,
            @RequestParam String password){
        return Result.judge(sysUserService.updatePassword(id, password));
    }

    @Operation(summary = "注销登出")
    @DeleteMapping("/logout")
    public Result<Boolean> logout() {
        return Result.judge(sysUserService.logout());
    }

    /**
     * 添加用户
     *
     * @param form 用户表单
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加用户")
    @PreAuthorize("@ss.hasPerm('sys:user:save') or @ss.hasRole('ADMIN')")
    public Result<Boolean> save(@ParameterObject@Validated SysUserSaveForm form) {
        return Result.judge(sysUserService.saveUser(form));
    }

    /**
     * 修改用户
     *
     * @param form 用户表单
     * @return {@code true} 修改成功，{@code false} 修改失败
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户")
    @PreAuthorize("@ss.hasPerm('sys:user:update')")
    public Result<Boolean> update(@ParameterObject@Validated SysUserUpdateForm form) {
        return Result.judge(sysUserService.updateUser(form));
    }

    /**
     * 根据主键删除用户表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("@ss.hasPerm('sys:user:delete')")
    public boolean remove(
            @Parameter(name = "id", description = "主键ID", required = true)
            @PathVariable Serializable id) {
        return sysUserService.removeById(id);
    }

    /**
     * 查询所有用户
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有用户")
    @PreAuthorize("@ss.hasPerm('sys:user:read')")
    public Result<List<SysUserVO>> list() {
        return Result.success(sysUserService.getUserList());
    }

    /**
     * 分页查询用户表
     *
     * @param query 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户表")
    @PreAuthorize("@ss.hasRole('ADMIN')")
    public Result<Page<SysUserVO>> page(@ParameterObject @Validated SysUserPageQuery query) {
        Page<SysUserVO> pageResult = sysUserService.getUserPage(query);
        sysRolePermissionService.refreshRolePermsCache();
        return Result.success(pageResult);
    }

    @GetMapping("/profile")
    @Operation(summary = "获取用户个人信息")
    @PreAuthorize("@ss.hasRole('USER')")
    public Result<SysUserProfileVO> getUserProfile() {
        SysUserProfileVO profile = sysUserService.getUserProfile();
        return Result.success(profile);
    }
}