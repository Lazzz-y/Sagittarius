package io.github.lazzz.sagittarius.system.model.request.form;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 字典类型
 *
 * @author Lazzz
 * @date 2025/10/15 14:10
 **/
@Data
@Schema(description = "字典类型")
public class SysDictTypeForm {

    @Schema(description="字典类型ID")
    private Long id;

    @Schema(description="类型名称")
    private String name;

    @Schema(description="类型编码")
    private String code;

    @Schema(description="类型状态(1:启用;0:禁用)")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
