package io.github.lazzz.sagittarius.system.mapper;

import io.github.lazzz.sagittarius.system.model.entity.SysMenuRelation;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限关联表 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface SysMenuRelationMapper extends BaseMapper<SysMenuRelation> {


}
