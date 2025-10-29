package io.github.lazzz.sagittarius.system.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictForm;
import io.github.lazzz.sagittarius.system.model.vo.SysDictVO;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;

import java.lang.String;
import java.lang.Integer;

/**
 * 实体类。
 *
 * @author Lazzz
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "数据字典表")
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_dict")
@AutoMappers(
        value = {
                @AutoMapper(target = SysDictVO.class),
                @AutoMapper(target = SysDictForm.class),
//                @AutoMapper(target = DictDetailDTO.class)
        }
)
public class SysDict extends BaseSnowflakeEntity {

    /* 字典类型编码 */
    @Column(value = "type_code")
    private String typeCode;

    /* 字典名称 */
    @Column(value = "name")
    private String name;

    /* 字典值 */
    @Column(value = "value")
    private String value;

    /* 排序 */
    @Column(value = "sort")
    private Integer sort;

    /* 状态(1:正常;0:禁用) */
    @Column(value = "status")
    private Integer status;

    /* 是否默认(1:是;0:否) */
    @Column(value = "defaulted")
    private Integer defaulted;

    /* 备注 */
    @Column(value = "remark")
    private String remark;

}
