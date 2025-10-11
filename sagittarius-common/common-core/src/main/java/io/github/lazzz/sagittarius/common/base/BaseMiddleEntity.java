package io.github.lazzz.sagittarius.common.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 中间实体基类
 * 
 * @author Lazzz 
 * @date 2025/10/10 11:07
**/
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseMiddleEntity extends BaseEntity{

    /**
     * 租户ID
     */
    @Column(tenantId = true)
    private Long tenantId;

}

