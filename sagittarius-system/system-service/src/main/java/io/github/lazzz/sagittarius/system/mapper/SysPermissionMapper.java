package io.github.lazzz.sagittarius.system.mapper;

import io.github.lazzz.sagittarius.system.model.entity.SysPermission;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统权限表 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {


}
