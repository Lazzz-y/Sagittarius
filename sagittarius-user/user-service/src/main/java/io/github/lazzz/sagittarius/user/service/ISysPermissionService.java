package io.github.lazzz.sagittarius.user.service;


import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.user.model.request.form.SysPermForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysPermPageQuery;
import io.github.lazzz.sagittarius.user.model.vo.SysPermVO;

import java.util.List;

/**
 * 系统权限表 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysPermissionService extends IService<SysPermission> {


    /**
     * 保存或更新系统权限
     *
     * @author Lazzz
     * @date 2025/10/12
     * @param form 表单数据
     * @return {@link Boolean}
     */
    boolean saveOrUpdatePerm(SysPermForm form);

    /**
     * 删除系统权限
     *
     * @author Lazzz
     * @date 2025/10/12
     * @param ids 权限Id，多个用逗号分隔
     * @return {@link Boolean}
     */
    boolean deletePerms(String ids);

    /**
     * 获取系统权限分页列表
     *
     * @author Lazzz
     * @date 2025/10/12
     * @param query 查询参数
     * @return {@link Page<SysPermVO>}
     */
    Page<SysPermVO> getPermsPage(SysPermPageQuery query);

    /**
     * 分配菜单权限
     *
     * @author Lazzz
     * @date 2025/10/12
     * @param permId 权限Id
     * @param menuIds 菜单Id集合
     * @return {@link Boolean}
     */
    boolean assignMenuToPerm(Long permId, List<Long> menuIds);
}