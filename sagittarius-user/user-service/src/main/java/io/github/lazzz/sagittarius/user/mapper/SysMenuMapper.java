package io.github.lazzz.sagittarius.user.mapper;

import io.github.lazzz.sagittarius.user.model.bo.RouteBO;
import io.github.lazzz.sagittarius.user.model.entity.SysMenu;
import com.mybatisflex.core.BaseMapper;
import io.github.lazzz.sagittarius.user.model.vo.RouteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

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
