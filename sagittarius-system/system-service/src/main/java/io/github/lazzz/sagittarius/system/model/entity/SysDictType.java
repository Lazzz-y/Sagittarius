package io.github.lazzz.sagittarius.system.model.entity;

import io.github.lazzz.sagittarius.common.base.BaseSnowflakeEntity;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictTypeForm;
import io.github.lazzz.sagittarius.system.model.vo.SysDictTypeVO;
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
@Schema(name = "数据字典类型表")
@Table(value = "sys_dict_type")
@EqualsAndHashCode(callSuper = true)
@AutoMappers(value = {
        @AutoMapper(target = SysDictTypeVO.class),
        @AutoMapper(target = SysDictTypeForm.class)
})
public class SysDictType extends BaseSnowflakeEntity {

    @Column(value = "name")
    private String name;

    @Column(value = "code")
    private String code;

    @Column(value = "status")
    private Integer status;

    @Column(value = "remark")
    private String remark;

}
