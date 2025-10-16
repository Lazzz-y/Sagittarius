package io.github.lazzz.sagittarius.system.model.request.query;


import io.github.lazzz.sagittarius.common.base.BasePageQuery;
import io.github.lazzz.sagittarius.system.model.entity.SysDictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型分页查询对象
 *
 * @author Lazzz
 * @date 2025/10/15 14:15
 **/
@Data
@Schema(description ="字典类型分页查询对象")
@EqualsAndHashCode(callSuper = true)
public class SysDictTypePageQuery extends BasePageQuery<SysDictType> {

    @Schema(description="关键字(类型名称/类型编码)")
    private String keywords;

}
