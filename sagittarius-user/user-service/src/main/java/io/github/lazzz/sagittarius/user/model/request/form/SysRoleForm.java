package io.github.lazzz.sagittarius.user.model.request.form;


import io.github.lazzz.sagittarius.user.model.entity.SysRole;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色表单
 * 
 * @author Lazzz 
 * @date 2025/10/10 15:54
**/
@Data
@AutoMapper(target = SysRole.class)
@Schema(name = "角色表单对象")
public class SysRoleForm {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long id;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    private String description;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer sort;

}

