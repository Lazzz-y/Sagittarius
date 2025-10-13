package io.github.lazzz.sagittarius.user.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 菜单路由视图对象
 * 
 * @author Lazzz 
 * @date 2025/10/13 13:55
**/
@Data
@Schema(description = "菜单路由视图对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteVO {

    @Schema(description = "路由路径", example = "role")
    private String path;

    @Schema(description = "组件路径", example = "system/user/index")
    private String component;

    @Schema(description = "跳转链接", example = "https://www.google.com")
    private String redirect;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "路由元数据")
    private Meta meta;

    @Data
    @Schema(description = "路由元数据类型")
    public static class Meta{

        @Schema(description = "路由标题", example = "用户管理")
        private String title;

        @Schema(description = "路由图标", example = "icon-user")
        private String icon;

        @Schema(description = "是否隐藏(true-是 false-否)", example = "true")
        private Boolean hidden;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "[菜单]是否缓存(true-是 false-否)", example = "true")
        private Boolean keepAlive;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "[目录]只有一个子路由是否始终显示(true-是 false-否)", example = "true")
        private Boolean alwaysShow;

        @Schema(description = "拥有路由权限的角色编码", example = "['SUPER_ADMIN','ADMIN']")
        private List<String> roles;
    }

    @Schema(description = "子路由列表")
    private List<RouteVO> children;
}

