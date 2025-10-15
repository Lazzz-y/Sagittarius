package io.github.lazzz.sagittarius.system.controller;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.common.web.annotation.PreventDuplicateResubmit;
import io.github.lazzz.sagittarius.common.annotation.RefreshableController;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.system.model.request.form.SysPermForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysPermPageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysPermVO;
import io.github.lazzz.sagittarius.system.service.ISysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * 系统权限表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@RefreshableController
@RequestMapping("/api/v1/perm")
@Tag(name = "03.权限接口")
@RequiredArgsConstructor
public class SysPermissionController {

    private final ISysPermissionService sysPermissionService;

    @PostMapping
    @Operation(summary = "新增权限")
    @PreventDuplicateResubmit
    @PreAuthorize("@ss.hasPerm('sys:perm:add')")
    public Result<Boolean> save(@RequestBody @Valid SysPermForm form) {
        return Result.judge(sysPermissionService.saveOrUpdatePerm(form));
    }

    @Operation(summary = "修改权限")
    @PutMapping(value = "/{permId}")
    @PreventDuplicateResubmit
    @PreAuthorize("@ss.hasPerm('sys:perm:edit')")
    public Result<Boolean> update(
            @Parameter(description = "权限ID")
            @PathVariable Long permId,
            @RequestBody @Valid SysPermForm form
    ) {
        form.setId(permId);
        return Result.judge(sysPermissionService.saveOrUpdatePerm(form));
    }

    @Operation(summary = "删除权限")
    @DeleteMapping(value = "/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:perm:delete')")
    public Result<Boolean> delete(
            @Parameter(description = "删除权限，多个以英文逗号(,)分割")
            @PathVariable String ids) {
        return Result.judge(sysPermissionService.deletePerms(ids));
    }

    @Operation(summary = "权限分页查询")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('sys:perm:query')")
    public Result<Page<SysPermVO>> page(@ParameterObject SysPermPageQuery query) {
        return Result.success(sysPermissionService.getPermsPage(query));
    }

    @Operation(summary = "权限分配菜单")
    @PutMapping("/{permId}/menus")
    @PreAuthorize("@ss.hasPerm('sys:perm:menu:assign')")
    @PreventDuplicateResubmit
    public Result<Boolean> assignMenus(
            @PathVariable Long permId,
            @RequestBody List<Long> menuIds
    ) {
        return Result.judge(sysPermissionService.assignMenuToPerm(permId, menuIds));
    }

}