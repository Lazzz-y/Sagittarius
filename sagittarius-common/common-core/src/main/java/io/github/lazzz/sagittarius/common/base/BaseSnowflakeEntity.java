package io.github.lazzz.sagittarius.common.base;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 雪花ID实体基类
 *
 * @author Lazzz
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseSnowflakeEntity extends BaseEntity{

    /**
     * 主键ID
     */
    @Column(value = "id")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 租户ID
     */
    @Column(tenantId = true)
    private Long tenantId;

}