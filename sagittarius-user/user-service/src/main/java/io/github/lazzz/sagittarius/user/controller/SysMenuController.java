package io.github.lazzz.sagittarius.user.controller;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import io.github.lazzz.common.web.annotation.PreventDuplicateResubmit;
import io.github.lazzz.sagittarius.common.cache.constant.CacheConstants;
import io.github.lazzz.sagittarius.user.model.request.form.SysMenuForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysMenuQuery;
import io.github.lazzz.sagittarius.user.model.vo.RouteVO;
import io.github.lazzz.sagittarius.user.model.vo.SysMenuVO;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.github.lazzz.sagittarius.user.service.ISysMenuService;

import java.util.List;

import io.github.lazzz.sagittarius.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统菜单表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/menus")
@Tag(name = "04.菜单路由接口")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @Operation(summary = "菜单列表")
    @GetMapping
    public Result<List<SysMenuVO>> listMenus(@ParameterObject SysMenuQuery query) {
        return Result.success(sysMenuService.listMenuVO(query));
    }

    @Operation(summary = "路由列表")
    @GetMapping("/routes")
    @Cached(name = "menu:", key = CacheConstants.MENU_KEY, expire = 30, localExpire = 15,cacheType = CacheType.BOTH)
    public Result<List<RouteVO>> listRoutes() {
        List<RouteVO> routeList = sysMenuService.listRoutes();
        return Result.success(routeList);
    }

    @PostMapping
    @PreventDuplicateResubmit
    @Operation(summary = "新增菜单")
    @PreAuthorize("@ss.hasPerm('sys:menu:add')")
    @CacheUpdate(name = "menu:", key = CacheConstants.MENU_KEY, value = "#result")    public Result<List<RouteVO>> saveMenu(@RequestBody SysMenuForm form) {
        return Result.success(sysMenuService.saveOrUpdateMenu(form));
    }

    @PreventDuplicateResubmit
    @Operation(summary = "修改菜单")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('sys:menu:edit')")
    @CacheUpdate(name = "menu:", key = CacheConstants.MENU_KEY, value = "#result")
    public Result<List<RouteVO>> updateMenu(
            @PathVariable Long id,
            @RequestBody SysMenuForm form
    ) {
        form.setId(id);
        return Result.success(sysMenuService.saveOrUpdateMenu(form));
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:menu:delete')")
    @CacheInvalidate(name = "menu:", key = CacheConstants.MENU_KEY)
    public Result<List<RouteVO>> deleteMenu(
            @Parameter(description ="菜单ID") @PathVariable("id") Long id
    ) {
        return Result.success(sysMenuService.deleteMenu(id));
    }
}