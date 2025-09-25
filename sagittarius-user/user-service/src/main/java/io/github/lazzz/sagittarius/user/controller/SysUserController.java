package io.github.lazzz.sagittarius.user.controller;

import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.user.dto.UserAuthDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.lazzz.sagittarius.user.service.SysUserService;
import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户表控制层")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "获取用户认证信息", hidden = false)
    @RequestMapping("/{username}/auth")
    public Result<UserAuthDTO> getUserAuthDTO(
            @Parameter(description = "用户名") @PathVariable String username) {
        UserAuthDTO userAuthDTO = sysUserService.getUserAuthDTO(username);
        return Result.success(userAuthDTO);
    }

    /**
     * 添加 用户表
     *
     * @param sysUser 用户表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加用户表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true),
            @Parameter(name = "username", description = "用户名", required = true),
            @Parameter(name = "nickname", description = "昵称", required = true),
            @Parameter(name = "password", description = "密码", required = true),
            @Parameter(name = "email", description = "邮箱"),
            @Parameter(name = "phone", description = "手机号"),
            @Parameter(name = "avatar", description = "头像"),
            @Parameter(name = "status", description = "状态（0-禁用，1-启用）", required = true),
            @Parameter(name = "userType", description = "用户类型（0-普通用户，1-博客作者，2-内容管理员）"),
            @Parameter(name = "bio", description = "个人简介"),
            @Parameter(name = "website", description = "个人网站"),
            @Parameter(name = "articleCount", description = "文章数量"),
            @Parameter(name = "commentCount", description = "评论数量"),
            @Parameter(name = "viewCount", description = "被访问次数"),
            @Parameter(name = "createTime", description = "创建时间", required = true),
            @Parameter(name = "updateTime", description = "更新时间", required = true),
            @Parameter(name = "createBy", description = "创建人ID"),
            @Parameter(name = "updateBy", description = "更新人ID"),
            @Parameter(name = "deleted", description = "逻辑删除标识(0:未删除;1:已删除)")
    })
    public boolean save(@RequestBody SysUser sysUser) {
        return sysUserService.save(sysUser);
    }


    /**
     * 根据主键删除用户表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除用户表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true)
    })
    public boolean remove(@PathVariable Serializable id) {
        return sysUserService.removeById(id);
    }


    /**
     * 根据主键更新用户表
     *
     * @param sysUser 用户表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新用户表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true),
            @Parameter(name = "username", description = "用户名"),
            @Parameter(name = "nickname", description = "昵称"),
            @Parameter(name = "password", description = "密码"),
            @Parameter(name = "email", description = "邮箱"),
            @Parameter(name = "phone", description = "手机号"),
            @Parameter(name = "avatar", description = "头像"),
            @Parameter(name = "status", description = "状态（0-禁用，1-启用）"),
            @Parameter(name = "userType", description = "用户类型（0-普通用户，1-博客作者，2-内容管理员）"),
            @Parameter(name = "bio", description = "个人简介"),
            @Parameter(name = "website", description = "个人网站"),
            @Parameter(name = "articleCount", description = "文章数量"),
            @Parameter(name = "commentCount", description = "评论数量"),
            @Parameter(name = "viewCount", description = "被访问次数"),
            @Parameter(name = "createTime", description = "创建时间"),
            @Parameter(name = "updateTime", description = "更新时间"),
            @Parameter(name = "createBy", description = "创建人ID"),
            @Parameter(name = "updateBy", description = "更新人ID"),
            @Parameter(name = "deleted", description = "逻辑删除标识(0:未删除;1:已删除)")
    })
    public boolean update(@RequestBody SysUser sysUser) {
        return sysUserService.updateById(sysUser);
    }


    /**
     * 查询所有用户表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有用户表")
    public List<SysUser> list() {
        return sysUserService.list();
    }


    /**
     * 根据用户表主键获取详细信息。
     *
     * @param id sysUser主键
     * @return 用户表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据用户表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true)
    })
    public SysUser getInfo(@PathVariable Serializable id) {
        return sysUserService.getById(id);
    }


    /**
     * 分页查询用户表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public Page<SysUser> page(Page<SysUser> page) {
        return sysUserService.page(page);
    }
}