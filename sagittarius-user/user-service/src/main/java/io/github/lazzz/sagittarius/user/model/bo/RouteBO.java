package io.github.lazzz.sagittarius.user.model.bo;


import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.github.lazzz.sagittarius.user.enums.MenuTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 路由表复合对象
 * 
 * @author Lazzz 
 * @date 2025/10/13 11:46
**/
@Data
public class RouteBO {

    /**
     * 菜单ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 是否可见: 1-是, 0-否
     */
    private Integer isVisible;

    /**
     * 菜单类型
     */
    private MenuTypeEnum menuType;

    /**
     * [目录]只有一个子路由是否始终显示(1:是 0:否)
     */
    private Integer alwaysShow;

    /**
     * [目录]是否开启页面缓存(1:是 0:否)
     */
    private Integer keepAlive;

    /**
     * 跳转路径
     */
    private String redirect;

    /**
     * 拥有菜单的角色
     */
    private List<String> roles;

}

