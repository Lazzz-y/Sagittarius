package io.github.lazzz.sagittarius.user.model.request.form;


import io.github.lazzz.sagittarius.user.model.entity.SysPermission;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 权限表单
 * 
 * @author Lazzz 
 * @date 2025/10/12 20:07
**/
@Data
@Schema(name = "权限表单对象")
@AutoMapper(target = SysPermission.class)
public class SysPermForm {

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long id;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    @NotBlank(message = "权限编码不能为空")
    private String permCode;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String permName;

    /**
     * 资源类型
     */
    @Schema(description = "资源类型")
    @NotBlank(message = "资源类型不能为空")
    private String resourceType;

    /**
     * 资源ID, *表示所有
     */
    @Schema(description = "资源ID, *表示所有")
    private String resourceId;

    /**
     * 操作类型
     */
    @Schema(description = "操作类型")
    private String action;

    /**
     * 权限描述
     */
    @Schema(description = "权限描述")
    private String description;

}

