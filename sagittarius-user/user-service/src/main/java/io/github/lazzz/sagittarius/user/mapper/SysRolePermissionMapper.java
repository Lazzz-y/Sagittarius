package io.github.lazzz.sagittarius.user.mapper;

import io.github.lazzz.sagittarius.user.model.bo.RolePermsBO;
import io.github.lazzz.sagittarius.user.model.entity.SysRolePermission;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色权限关联表 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

}
