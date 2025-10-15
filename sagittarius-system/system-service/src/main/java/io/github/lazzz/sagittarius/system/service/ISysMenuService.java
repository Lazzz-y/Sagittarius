package io.github.lazzz.sagittarius.system.service;


import io.github.lazzz.sagittarius.system.model.entity.SysMenu;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.system.model.request.form.SysMenuForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysMenuQuery;
import io.github.lazzz.sagittarius.system.model.vo.RouteVO;
import io.github.lazzz.sagittarius.system.model.vo.SysMenuVO;

import java.util.List;

/**
 * 系统菜单表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单列表
     *
     * @author Lazzz
     * @date 2025/10/13
     * @param query 菜单查询参数
     * @return {@link List<SysMenuVO>}
     */
    List<SysMenuVO> listMenuVO(SysMenuQuery query);

    /**
     * 保存或更新菜单
     *
     * @author Lazzz
     * @date 2025/10/13
     * @param form 菜单表单
     * @return {@link List<RouteVO>}
     */
    List<RouteVO> saveOrUpdateMenu(SysMenuForm form);

    /**
     * 删除菜单
     *
     * @author Lazzz
     * @date 2025/10/13
     * @param menuId 菜单ID
     * @return {@link Boolean}
     */
    List<RouteVO> deleteMenu(Long menuId);

    /**
     * 获取菜单路由
     *
     * @author Lazzz
     * @date 2025/10/13
     * @return {@link List<RouteVO>}
     */
    List<RouteVO> listRoutes();

}