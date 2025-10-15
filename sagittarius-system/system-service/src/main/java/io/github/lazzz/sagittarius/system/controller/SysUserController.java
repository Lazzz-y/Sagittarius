package io.github.lazzz.sagittarius.system.controller;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.common.security.util.SecurityUtils;
import io.github.lazzz.common.web.annotation.PreventDuplicateResubmit;
import io.github.lazzz.sagittarius.common.annotation.RefreshableController;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.system.model.entity.SysUser;
import io.github.lazzz.sagittarius.system.model.request.form.SysUserSaveForm;
import io.github.lazzz.sagittarius.system.model.request.form.SysUserUpdateForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysUserPageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysUserProfileVO;
import io.github.lazzz.sagittarius.system.model.vo.SysUserVO;
import io.github.lazzz.sagittarius.system.service.ISysUserRoleService;
import io.github.lazzz.sagittarius.system.service.ISysUserService;
import io.github.lazzz.system.dto.UserAuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 用户表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Slf4j
@RefreshableController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "01.用户接口")
public class SysUserController {

    private final ISysUserService sysUserService;

    private final ISysUserRoleService sysUserRoleService;

    @Operation(summary = "获取用户认证信息", hidden = true)
    @RequestMapping("/{username}/auth")
    public Result<UserAuthDTO> getUserAuthDTO(
            @Parameter(description = "用户名") @PathVariable String username) {
        UserAuthDTO userAuthDTO = sysUserService.getUserAuthDTO(username);
        return Result.success(userAuthDTO);
    }

    @Operation(summary = "注销登出")
    @DeleteMapping("/logout")
    public Result<Boolean> logout() {
        return Result.judge(sysUserService.logout());
    }

    @Operation(summary = "修改密码(用户)")
    @PatchMapping("/update/password")
    @PreAuthorize("@ss.hasPerm('sys:user:password:user')")
    public Result<Boolean> updatePassword(@RequestParam @NotBlank(message = "密码不能为空") String password){
        // 从上下文中获取用户ID
        Long userId = SecurityUtils.getUserId();
        return Result.judge(sysUserService.updatePassword(userId, password));
    }

    @Operation(summary = "修改密码(管理员)")
    @PatchMapping("/{id}/password")
    @PreAuthorize("@ss.hasPerm('sys:user:password:admin')")
    public Result<Boolean> updatePassword(
            @PathVariable @NotNull(message = "用户ID不能为空") Long id,
            @RequestParam @NotBlank(message = "密码不能为空") String password){
        return Result.judge(sysUserService.updatePassword(id, password));
    }


    /**
     * 添加用户
     *
     * @param form 用户表单
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加用户")
    @PreAuthorize("@ss.hasPerm('sys:user:add')")
    public Result<Boolean> save(@ParameterObject@Validated SysUserSaveForm form) {
        return Result.judge(sysUserService.saveUser(form));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "修改用户")
    @PreAuthorize("@ss.hasPerm('sys:user:edit')")
    @PreventDuplicateResubmit
    public Result<Boolean> edit(@PathVariable Long userId, @RequestBody SysUserUpdateForm form) {
        return Result.judge(sysUserService.updateUser(userId, form));
    }

    @Operation(summary = "修改用户状态")
    @PatchMapping("/{userId}/status")
    @PreAuthorize("@ss.hasPerm('sys:user:status')")
    public Result<Boolean> updateStatus(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "用户状态(1:启用;0:禁用)") @RequestParam Integer status
    ) {
        var result = sysUserService.updateChain()
                .eq(SysUser::getId, userId)
                .set(SysUser::getStatus, status)
                .update();
        return Result.judge(result);
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
     * 分页查询用户表
     *
     * @param query 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户表")
    @PreAuthorize("@ss.hasPerm('sys:user:query')")
    public Result<Page<SysUserVO>> page(@ParameterObject @Validated SysUserPageQuery query) {
        log.info("分页查询用户表: {}", query);
        Page<SysUserVO> pageResult = sysUserService.getUserPage(query);
        return Result.success(pageResult);
    }

    @GetMapping("/profile")
    @Operation(summary = "获取用户个人信息")
    @PreAuthorize("@ss.hasPerm('sys:user:profile')")
    public Result<SysUserProfileVO> getUserProfile() {
        SysUserProfileVO profile = sysUserService.getUserProfile();
        return Result.success(profile);
    }

    @PutMapping("/{userId}/role")
    @Operation(summary = "用户分配角色")
    @PreAuthorize("@ss.hasPerm('sys:user:role:assign')")
    @PreventDuplicateResubmit
    public Result<Boolean> assignRole(
            @Parameter(name = "userId", description = "用户Id", required = true)
            @PathVariable Long userId,
            @Parameter(name = "roleIds", description = "角色Id列表", required = true)
            @RequestBody List<Long> roleIds) {
        return Result.judge(sysUserRoleService.assignRoleToUser(userId, roleIds));
    }
}