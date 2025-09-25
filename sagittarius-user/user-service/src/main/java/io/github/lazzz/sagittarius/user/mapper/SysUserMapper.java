package io.github.lazzz.sagittarius.user.mapper;

import io.github.lazzz.sagittarius.user.model.entity.SysUser;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


}
