package io.github.lazzz.sagittarius.system.mapper;

import io.github.lazzz.sagittarius.system.model.entity.SysRolePermission;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联表 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

}
