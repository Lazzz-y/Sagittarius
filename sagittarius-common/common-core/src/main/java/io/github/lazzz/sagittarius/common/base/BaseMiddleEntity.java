package io.github.lazzz.sagittarius.common.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 抽象中间实体类
 * 
 * @author Lazzz 
 * @date 2025/10/10 11:07
**/
@Data
public class BaseMiddleEntity {

    /**
     * 租户ID
     */
    @Column(tenantId = true)
    private Long tenantId;

    /**
     * 创建时间
     */
    @Column(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    @Column(value = "create_by")
    private Long createBy;

    /**
     * 更新人ID
     */
    @Column(value = "update_by")
    private Long updateBy;

    /**
     * 逻辑删除标志
     * 0:未删除 1:已删除
     */
    @Column(value = "deleted", isLogicDelete = true)
    private Integer deleted;

}

