package io.github.lazzz.sagittarius.system.service;


import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.common.web.model.Option;
import io.github.lazzz.sagittarius.system.model.entity.SysDictType;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictTypeForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysDictTypePageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysDictTypeVO;

import java.util.List;

/**
 * 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysDictTypeService extends IService<SysDictType> {

    /**
     * 字典类型分页查询
     *
     * @param query 查询参数
     * @return {@link Page<SysDictTypeVO>} 字典类型分页数据
     */
    Page<SysDictTypeVO> getDictTypePage(SysDictTypePageQuery query);

    /**
     * 字典类型表单查询
     *
     * @param id 字典类型ID
     * @return {@link SysDictTypeForm} 字典类型表单数据
     */
    SysDictTypeForm getDictTypeForm(Long id);

    /**
     * 保存字典类型
     *
     * @param dictTypeForm 字典类型表单数据
     * @return {@link Boolean} 是否保存成功
     */
    boolean saveDictType(SysDictTypeForm dictTypeForm);

    /**
     * 更新字典类型
     *
     * @param id 字典类型ID
     * @param dictTypeForm 字典类型表单数据
     * @return {@link Boolean} 是否更新成功
     */
    boolean updateDictType(Long id, SysDictTypeForm dictTypeForm);

    /**
     * 删除字典类型
     *
     * @param idsStr 字典类型ID
     * @return {@link Boolean} 是否删除成功
     */
    boolean deleteDictTypes(String idsStr);

    /**
     * 字典类型列表
     *
     * @param typeCode 字典类型编码
     * @return {@link List<Option>} 字典类型列表
     */
    List<Option<String>> listDictItemsByTypeCode(String typeCode);

}