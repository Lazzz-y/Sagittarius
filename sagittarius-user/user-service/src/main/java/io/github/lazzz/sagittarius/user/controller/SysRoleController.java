package io.github.lazzz.sagittarius.user.controller;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.common.web.annotation.PreventDuplicateResubmit;
import io.github.lazzz.sagittarius.common.annotation.RefreshableController;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.lazzz.sagittarius.user.model.request.form.SysRoleForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysRolePageQuery;
import io.github.lazzz.sagittarius.user.model.vo.SysRoleVO;
import io.github.lazzz.sagittarius.user.service.ISysRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

import io.github.lazzz.sagittarius.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Slf4j
@RefreshableController
@RequestMapping("/api/v1/roles")
@Tag(name = "02.角色接口")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:role:add')")
    @PreventDuplicateResubmit
    public Result<Boolean> saveRole(@RequestBody @Valid SysRoleForm form) {
        boolean result = sysRoleService.saveOrUpdateRole(form);
        return Result.judge(result);
    }

    @Operation(summary = "修改角色")
    @PutMapping(value = "/{roleId}")
    @PreAuthorize("@ss.hasPerm('sys:role:edit')")
    @PreventDuplicateResubmit
    public Result<Boolean> updateRole(
            @Parameter(description = "角色ID") @PathVariable Long roleId,
            @Valid @RequestBody SysRoleForm form) {
        form.setId(roleId);
        boolean result = sysRoleService.saveOrUpdateRole(form);
        return Result.judge(result);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:role:delete')")
    @PreventDuplicateResubmit
    public Result<Boolean> deleteRoles(
            @Parameter(description ="删除角色，多个以英文逗号(,)分割")
            @PathVariable String ids
    ) {
        boolean result = sysRoleService.deleteRoles(ids);
        return Result.judge(result);
    }

    @Operation(summary = "分页查询角色")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('sys:role:query')")
    public Result<Page<SysRoleVO>> getRolePage(@ParameterObject SysRolePageQuery query) {
        Page<SysRoleVO> page = sysRoleService.getRolePage(query);
        return Result.success(page);
    }

    @Operation(summary = "角色分配权限")
    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("@ss.hasPerm('sys:role:perm:assign')")
    @PreventDuplicateResubmit
    public Result<Boolean> assignPermToRole(@PathVariable Long roleId, @RequestBody List<Long> permIds) {
        boolean result = sysRoleService.assignPermToRole(roleId, permIds);
        return Result.judge(result);
    }
}