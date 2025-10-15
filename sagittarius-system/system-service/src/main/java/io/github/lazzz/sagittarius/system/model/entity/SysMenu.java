package io.github.lazzz.sagittarius.system.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
import io.github.lazzz.sagittarius.system.model.request.form.SysMenuForm;
import io.github.lazzz.sagittarius.system.model.vo.SysMenuVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;

/**
 * 系统菜单表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统菜单表")
@Table(value = "sys_menu")
@EqualsAndHashCode(callSuper = true)
@AutoMappers(value = {
        @AutoMapper(target = SysMenuVO.class),
        @AutoMapper(target = SysMenuForm.class),
})
public class SysMenu extends BaseSnowflakeEntity {

    /**
     * 父级ID
     */
    @Column(value = "parent_id")
    @Schema(description = "父级ID")
    private Long parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    @Column(value = "name")
    private String name;

    /**
     * 路由路径
     */
    @Schema(description = "路由路径")
    @Column(value = "path")
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    @Schema(description = "组件路径(vue页面完整路径，省略.vue后缀)")
    @Column(value = "component")
    private String component;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    @Column(value = "icon")
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @Column(value = "sort_order")
    private Integer sortOrder;

    /**
     * 是否可见: 1-是, 0-否
     */
    @Schema(description = "是否可见: 1-是, 0-否")
    @Column(value = "is_visible")
    private Integer isVisible;

    /**
     * 菜单类型:1-菜单,2-目录,3-按钮,4-外链
     */
    @Schema(description = "菜单类型:1-菜单,2-目录,3-按钮,4-外链")
    @Column(value = "menu_type")
    private Integer menuType;

    /**
     * [目录]只有一个子路由是否始终显示(1:是 0:否)
     */
    @Schema(description = "[目录]只有一个子路由是否始终显示(1:是 0:否)")
    @Column(value = "always_show")
    private Integer alwaysShow;

    /**
     * [目录]是否开启页面缓存(1:是 0:否)
     */
    @Schema(description = "[目录]是否开启页面缓存(1:是 0:否)")
    @Column(value = "keep_alive")
    private Integer keepAlive;

    /**
     * 跳转路径
     */
    @Schema(description = "跳转路径")
    @Column(value = "redirect")
    private String redirect;

    /**
     * 菜单构建路径
     */
    @Schema(description = "菜单构建路径")
    @Column(value = "tree_path")
    private String treePath;
}
