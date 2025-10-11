package io.github.lazzz.sagittarius.user.controller;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.common.annotation.RefreshableController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.lazzz.sagittarius.user.service.ISysPermissionService;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统权限表 控制层。
 *
 * @author Lazzz
 * @since 1.0
 */
@RefreshableController
@RequestMapping("/sysPermission")
@Tag(name = "系统权限表控制层")
@RequiredArgsConstructor
public class SysPermissionController {

    private final ISysPermissionService sysPermissionService;

    /**
     * 添加 系统权限表
     *
     * @param sysPermission 系统权限表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加系统权限表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true),

            @Parameter(name = "permCode", description = "权限编码", required = true),

            @Parameter(name = "permName", description = "权限名称", required = true),

            @Parameter(name = "resourceType", description = "资源类型", required = true),

            @Parameter(name = "resourceId", description = "资源ID, *表示所有"),

            @Parameter(name = "action", description = "操作类型", required = true),

            @Parameter(name = "description", description = "权限描述"),

            @Parameter(name = "createTime", description = "创建时间", required = true),

            @Parameter(name = "updateTime", description = "更新时间", required = true),

            @Parameter(name = "createBy", description = "创建人ID"),

            @Parameter(name = "updateBy", description = "更新人ID"),

            @Parameter(name = "deleted", description = "逻辑删除标识(0:未删除;1:已删除)", required = true)
    })

    public boolean save(@RequestBody SysPermission sysPermission) {
        return sysPermissionService.save(sysPermission);
    }


    /**
     * 根据主键删除系统权限表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除系统权限表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public boolean remove(@PathVariable Serializable id) {
        return sysPermissionService.removeById(id);
    }


    /**
     * 根据主键更新系统权限表
     *
     * @param sysPermission 系统权限表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新系统权限表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true),

            @Parameter(name = "permCode", description = "权限编码"),

            @Parameter(name = "permName", description = "权限名称"),

            @Parameter(name = "resourceType", description = "资源类型"),

            @Parameter(name = "resourceId", description = "资源ID, *表示所有"),

            @Parameter(name = "action", description = "操作类型"),

            @Parameter(name = "description", description = "权限描述"),

            @Parameter(name = "createTime", description = "创建时间"),

            @Parameter(name = "updateTime", description = "更新时间"),

            @Parameter(name = "createBy", description = "创建人ID"),

            @Parameter(name = "updateBy", description = "更新人ID"),

            @Parameter(name = "deleted", description = "逻辑删除标识(0:未删除;1:已删除)")
    })
    public boolean update(@RequestBody SysPermission sysPermission) {
        return sysPermissionService.updateById(sysPermission);
    }


    /**
     * 查询所有系统权限表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有系统权限表")
    public List<SysPermission> list() {
        return sysPermissionService.list();
    }


    /**
     * 根据系统权限表主键获取详细信息。
     *
     * @param id sysPermission主键
     * @return 系统权限表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据系统权限表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public SysPermission getInfo(@PathVariable Serializable id) {
        return sysPermissionService.getById(id);
    }


    /**
     * 分页查询系统权限表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询系统权限表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public Page<SysPermission> page(Page<SysPermission> page) {
        return sysPermissionService.page(page);
    }
}