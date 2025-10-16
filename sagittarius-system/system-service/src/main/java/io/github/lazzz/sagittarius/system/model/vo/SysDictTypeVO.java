package io.github.lazzz.sagittarius.system.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典类型视图对象
 *
 * @author Lazzz
 * @date 2025/10/15 14:26
 **/
@Data
@Schema(description ="字典类型视图对象")
public class SysDictTypeVO {

    @Schema(description="字典类型ID")
    private Long id;

    @Schema(description="类型名称")
    private String name;

    @Schema(description="类型编码")
    private String code;

    @Schema(description="状态：1:启用;0:禁用")
    private Integer status;

}
