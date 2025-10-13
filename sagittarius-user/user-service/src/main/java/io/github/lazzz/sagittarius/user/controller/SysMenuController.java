package io.github.lazzz.sagittarius.user.controller;

import io.github.lazzz.sagittarius.user.model.request.query.SysMenuQuery;
import io.github.lazzz.sagittarius.user.model.vo.RouteVO;
import io.github.lazzz.sagittarius.user.model.vo.SysMenuVO;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.lazzz.sagittarius.user.service.ISysMenuService;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/sysMenu")
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
    public Result<List<RouteVO>> listRoutes() {
        List<RouteVO> routeList = sysMenuService.listRoutes();
        return Result.success(routeList);
    }
}