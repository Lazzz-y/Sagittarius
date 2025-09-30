package io.github.lazzz.sagittarius.user.controller;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.lazzz.sagittarius.user.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "02.角色接口")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService sysRoleService;


    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public Result<Boolean> remove(@PathVariable Serializable id) {
        return Result.success(sysRoleService.removeById(id));
    }

    /**
     * 查询所有
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有")
    public Result<List<SysRole>> list() {
        return Result.success(sysRoleService.list());
    }


    /**
     * 根据主键获取详细信息。
     *
     * @param id sysRole主键
     * @return 详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public Result<SysRole> getInfo(@PathVariable Serializable id) {
        return Result.success(sysRoleService.getById(id));
    }


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public Result<Page<SysRole>> page(Page<SysRole> page) {
        return Result.success(sysRoleService.page(page));
    }
}