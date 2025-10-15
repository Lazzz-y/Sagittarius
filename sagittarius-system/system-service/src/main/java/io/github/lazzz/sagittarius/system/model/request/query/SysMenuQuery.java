package io.github.lazzz.sagittarius.system.model.request.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 菜单查询对象
 * 
 * @author Lazzz 
 * @date 2025/10/13 10:44
**/
@Data
@Schema(description = "菜单查询对象")
public class SysMenuQuery {

    @Schema(description = "菜单名称")
    String name;

}

