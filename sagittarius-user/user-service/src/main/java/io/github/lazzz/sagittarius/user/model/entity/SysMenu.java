package io.github.lazzz.sagittarius.user.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
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
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_menu")
public class SysMenu extends BaseSnowflakeEntity {

    @Column(value = "parent_id")
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
     * 组件路径
     */
    @Schema(description = "组件路径")
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
     * 菜单类型
     */
    @Schema(description = "菜单类型")
    @Column(value = "menu_type")
    private Integer menuType;
}
