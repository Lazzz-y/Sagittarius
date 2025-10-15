package io.github.lazzz.sagittarius.system.mapper;

import io.github.lazzz.sagittarius.system.model.bo.RouteBO;
import io.github.lazzz.sagittarius.system.model.entity.SysMenu;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统菜单表 映射层。
 *
 * @author Lazzz
 * @since 1.0
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<RouteBO> listRoutes();

}
