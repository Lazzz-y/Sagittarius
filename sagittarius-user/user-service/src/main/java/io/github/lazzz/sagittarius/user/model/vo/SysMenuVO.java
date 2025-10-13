package io.github.lazzz.sagittarius.user.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lazzz.sagittarius.common.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 菜单表单对象
 * 
 * @author Lazzz 
 * @date 2025/10/13 10:28
**/
@Data
@Schema(description = "菜单表单对象")
@EqualsAndHashCode(callSuper = true)
public class SysMenuVO extends BaseVO {

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private Long id;

    /**
     * 父级ID
     */
    @Schema(description = "父级ID")
    private Long parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 路由路径
     */
    @Schema(description = "路由路径")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径(vue页面完整路径，省略.vue后缀)")
    private String component;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortOrder;

    /**
     * 是否可见: 1-是, 0-否
     */
    @Schema(description = "是否可见: 1-是, 0-否")
    private Integer isVisible;

    /**
     * 菜单类型:1-菜单,2-目录,3-按钮,4-外链
     */
    @Schema(description = "菜单类型:1-菜单,2-目录,3-按钮,4-外链")
    private Integer menuType;

    /**
     * [目录]只有一个子路由是否始终显示(1:是 0:否)
     */
    @Schema(description = "[目录]只有一个子路由是否始终显示(1:是 0:否)")
    private Integer alwaysShow;

    /**
     * [目录]是否开启页面缓存(1:是 0:否)
     */
    @Schema(description = "[目录]是否开启页面缓存(1:是 0:否)")
    private Integer keepAlive;

    /**
     * 跳转路径
     */
    @Schema(description = "跳转路径")
    private String redirect;

    /**
     * 子菜单
     */
    @Schema(description = "子菜单")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysMenuVO> children;
}

