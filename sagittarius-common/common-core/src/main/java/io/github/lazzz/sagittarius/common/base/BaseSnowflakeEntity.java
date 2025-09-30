package io.github.lazzz.sagittarius.common.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类基类
 * 包含公共字段
 *
 * @author Lazzz
 */
@Data
public class BaseSnowflakeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Column(value = "id")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

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