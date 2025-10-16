package io.github.lazzz.sagittarius.system.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典视图对象
 *
 * @author Lazzz
 * @date 2025/10/15 14:25
 **/
@Data
@Schema(description ="字典视图对象")
public class SysDictVO {

    @Schema(description="字典ID")
    private Long id;

    @Schema(description="字典名称")
    private String name;

    @Schema(description="字典值")
    private String value;

    @Schema(description="状态(1:启用;0:禁用)")
    private Integer status;

}
