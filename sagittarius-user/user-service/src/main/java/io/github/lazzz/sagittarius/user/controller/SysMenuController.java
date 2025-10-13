package io.github.lazzz.sagittarius.user.controller;

import lombok.RequiredArgsConstructor;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.lazzz.sagittarius.user.service.ISysMenuService;
import io.github.lazzz.sagittarius.user.model.entity.SysMenu;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

import io.github.lazzz.sagittarius.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统菜单表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@RestController
@RequestMapping("/sysMenu")
@Tag(name = "系统菜单表控制层")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    /**
     * 添加 系统菜单表
     *
     * @param sysMenu 系统菜单表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加系统菜单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true),

            @Parameter(name = "parentId", description = "", required = true),

            @Parameter(name = "name", description = "菜单名称", required = true),

            @Parameter(name = "path", description = "路由路径"),

            @Parameter(name = "component", description = "组件路径"),

            @Parameter(name = "icon", description = "菜单图标"),

            @Parameter(name = "sortOrder", description = "排序"),

            @Parameter(name = "isVisible", description = "是否可见: 1-是, 0-否"),

            @Parameter(name = "menuType", description = "菜单类型"),

            @Parameter(name = "tenantId", description = "", required = true),

            @Parameter(name = "createAt", description = "创建时间", required = true),

            @Parameter(name = "updateAt", description = "更新时间", required = true),

            @Parameter(name = "createBy", description = "创建人ID"),

            @Parameter(name = "updateBy", description = "更新人ID"),

            @Parameter(name = "deleted", description = "逻辑删除标识(0:未删除;1:已删除)", required = true)
    })

    public Result<Boolean> save(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.save(sysMenu));
    }


    /**
     * 根据主键删除系统菜单表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除系统菜单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public Result<Boolean> remove(@PathVariable Serializable id) {
        return Result.success(sysMenuService.removeById(id));
    }


    /**
     * 根据主键更新系统菜单表
     *
     * @param sysMenu 系统菜单表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新系统菜单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true),

            @Parameter(name = "parentId", description = ""),

            @Parameter(name = "name", description = "菜单名称"),

            @Parameter(name = "path", description = "路由路径"),

            @Parameter(name = "component", description = "组件路径"),

            @Parameter(name = "icon", description = "菜单图标"),

            @Parameter(name = "sortOrder", description = "排序"),

            @Parameter(name = "isVisible", description = "是否可见: 1-是, 0-否"),

            @Parameter(name = "menuType", description = "菜单类型"),

            @Parameter(name = "tenantId", description = ""),

            @Parameter(name = "createAt", description = "创建时间"),

            @Parameter(name = "updateAt", description = "更新时间"),

            @Parameter(name = "createBy", description = "创建人ID"),

            @Parameter(name = "updateBy", description = "更新人ID"),

            @Parameter(name = "deleted", description = "逻辑删除标识(0:未删除;1:已删除)")
    })
    public Result<Boolean> update(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.updateById(sysMenu));
    }


    /**
     * 查询所有系统菜单表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有系统菜单表")
    public Result<List<SysMenu>> list() {
        return Result.success(sysMenuService.list());
    }


    /**
     * 根据系统菜单表主键获取详细信息。
     *
     * @param id sysMenu主键
     * @return 系统菜单表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据系统菜单表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public Result<SysMenu> getInfo(@PathVariable Serializable id) {
        return Result.success(sysMenuService.getById(id));
    }


    /**
     * 分页查询系统菜单表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询系统菜单表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public Result<Page<SysMenu>> page(Page<SysMenu> page) {
        return Result.success(sysMenuService.page(page));
    }
}