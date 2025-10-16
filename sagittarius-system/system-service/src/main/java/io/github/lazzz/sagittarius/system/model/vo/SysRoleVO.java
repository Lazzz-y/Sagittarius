package io.github.lazzz.sagittarius.system.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.lazzz.sagittarius.common.base.BaseVO;
import io.github.lazzz.sagittarius.system.model.entity.SysRole;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * 角色视图对象
 * 
 * @author Lazzz 
 * @date 2025/10/10 16:32
**/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "角色视图对象")
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysRole.class)
public class SysRoleVO extends BaseVO {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long id;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String roleCode;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
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

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID")
    private Long createBy;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID")
    private Long updateBy;

}

