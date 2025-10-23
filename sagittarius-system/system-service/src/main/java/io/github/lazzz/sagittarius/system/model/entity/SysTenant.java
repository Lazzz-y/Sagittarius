package io.github.lazzz.sagittarius.system.model.entity;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.lang.Long;
import java.util.Date;
import java.sql.Timestamp;
import java.lang.Object;
import java.lang.String;
import java.lang.Integer;

/**
 * 租户信息表 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "租户信息表")
@Table(value = "sys_tenant")
public class SysTenant {

    /**
     * 租户ID（自增主键，全局唯一）
     */
    @Schema(description = "租户ID（自增主键，全局唯一）")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 租户唯一标识（用于域名/API隔离，如blog_enterprise_a）
     */
    @Schema(description = "租户唯一标识（用于域名/API隔离，如blog_enterprise_a）")
    @Column(value = "tenant_key")
    private String tenantKey;

    /**
     * 租户名称（如XX科技有限公司博客）
     */
    @Schema(description = "租户名称（如XX科技有限公司博客）")
    @Column(value = "tenant_name")
    private String tenantName;

    /**
     * 租户类型：1-个人租户，2-企业租户，3-团队租户
     */
    @Schema(description = "租户类型：1-个人租户，2-企业租户，3-团队租户")
    @Column(value = "tenant_type")
    private Integer tenantType;

    /**
     * 租户独立域名（如'blog.enterprise-a.com'，空表示使用平台二级域名）
     */
    @Schema(description = "租户独立域名（如'blog.enterprise-a.com'，空表示使用平台二级域名）")
    @Column(value = "domain")
    private String domain;

    /**
     * 租户Logo地址
     */
    @Schema(description = "租户Logo地址")
    @Column(value = "logo_url")
    private String logoUrl;

    /**
     * 租户主题配置（JSON格式，如{"color":"#337ab7","layout":"fluid"}）
     */
    @Schema(description = """
            租户主题配置（JSON格式，如{"
                color":"#337ab7","layout":"fluid"}）
            """)
    @Column(value = "theme_config")
    private Object themeConfig;

    /**
     * 联系人信息（JSON格式，如{"name":"张三","phone":"13800138000","email":"contact@enterprise-a.com"}）
     */
    @Schema(description = """
            联系人信息（JSON格式，如{"
                name":"张三","phone":"13800138000","email":"contact@enterprise-a.com"}）
            """)
    @Column(value = "contact_info")
    private Object contactInfo;

    /**
     * 计费方案：free-免费版，standard-标准版，enterprise-企业版
     */
    @Schema(description = "计费方案：free-免费版，standard-标准版，enterprise-企业版")
    @Column(value = "billing_plan")
    private String billingPlan;

    /**
     * 资源配额配置（JSON格式）：{"max_articles":1000,"max_storage":10737418240,"max_users":50}
     */
    @Schema(description = """
            资源配额配置（JSON格式）：{"
                max_articles":1000,"max_storage":10737418240,"max_users":50}
            """)
    @Column(value = "quota_config")
    private Object quotaConfig;

    /**
     * 已使用存储（字节，用于配额校验）
     */
    @Schema(description = "已使用存储（字节，用于配额校验）")
    @Column(value = "used_storage")
    private Long usedStorage;

    /**
     * 租户状态：0-禁用（不可访问），1-正常，2-过期（需续费），3-冻结（违规冻结）
     */
    @Schema(description = "租户状态：0-禁用（不可访问），1-正常，2-过期（需续费），3-冻结（违规冻结）")
    @Column(value = "status")
    private Integer status;

    /**
     * 过期时间（NULL表示永久有效，用于付费租户）
     */
    @Schema(description = "过期时间（NULL表示永久有效，用于付费租户）")
    @Column(value = "expire_time")
    private Date expireTime;

    /**
     * 冻结原因（仅status=3时有效）
     */
    @Schema(description = "冻结原因（仅status=3时有效）")
    @Column(value = "lock_reason")
    private String lockReason;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(value = "create_at")
    private Timestamp createAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Column(value = "update_at")
    private Timestamp updateAt;

    /**
     * 创建人ID（关联sys_user.id）
     */
    @Schema(description = "创建人ID（关联sys_user.id）")
    @Column(value = "create_by")
    private Long createBy;

    /**
     * 更新人ID（关联sys_user.id）
     */
    @Schema(description = "更新人ID（关联sys_user.id）")
    @Column(value = "update_by")
    private Long updateBy;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @Schema(description = "逻辑删除：0-未删除，1-已删除")
    @Column(value = "deleted")
    private Integer deleted;


}
