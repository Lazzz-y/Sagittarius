package io.github.lazzz.sagittarius.user.service;


import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.user.model.request.form.SysRoleForm;
import io.github.lazzz.sagittarius.user.model.request.query.SysRolePageQuery;
import io.github.lazzz.sagittarius.user.model.vo.SysRoleVO;

import java.util.List;

/**
 * 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 保存角色
     *
     * @param form 表单数据
     * @return {@link Boolean}
     */
    boolean saveOrUpdateRole(SysRoleForm form);

    /**
     * 删除角色
     *
     * @param ids 角色ID ,分割
     * @return {@link Boolean}
     */
    boolean deleteRoles(String ids);

    /**
     * 获取角色分页列表
     *
     * @param query 查询参数
     * @return {@link Page<SysRoleVO>}
     */
    Page<SysRoleVO> getRolePage(SysRolePageQuery query);

    /**
     * 分配权限
     *
     * @param roleId    角色ID
     * @param permIds  权限ID
     * @return {@link Boolean}
     */
    boolean assignPermToRole(Long roleId, List<Long> permIds);

}