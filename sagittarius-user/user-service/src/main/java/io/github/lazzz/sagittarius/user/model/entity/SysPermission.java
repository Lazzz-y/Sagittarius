package io.github.lazzz.sagittarius.user.model.entity;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.lang.Long;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
 * 系统权限表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统权限表")
@Table(value = "sys_permission")
public class SysPermission {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    @Column(value = "perm_code")
    private String permCode;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    @Column(value = "perm_name")
    private String permName;

    /**
     * 资源类型
     */
    @Schema(description = "资源类型")
    @Column(value = "resource_type")
    private String resourceType;

    /**
     * 资源ID, *表示所有
     */
    @Schema(description = "资源ID, *表示所有")
    @Column(value = "resource_id")
    private String resourceId;

    /**
     * 操作类型
     */
    @Schema(description = "操作类型")
    @Column(value = "action")
    private String action;

    /**
     * 权限描述
     */
    @Schema(description = "权限描述")
    @Column(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Column(value = "update_time")
    private Date updateTime;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID")
    @Column(value = "create_by")
    private Long createBy;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID")
    @Column(value = "update_by")
    private Long updateBy;

    /**
     * 逻辑删除标识(0:未删除;1:已删除)
     */
    @Schema(description = "逻辑删除标识(0:未删除;1:已删除)")
    @Column(value = "deleted")
    private Integer deleted;


}
