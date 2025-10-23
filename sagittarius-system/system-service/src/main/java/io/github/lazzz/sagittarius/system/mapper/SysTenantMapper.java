package io.github.lazzz.sagittarius.system.mapper;

import io.github.lazzz.sagittarius.system.model.entity.SysTenant;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户信息表 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {


}
