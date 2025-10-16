package io.github.lazzz.sagittarius.system.service;


import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.common.web.model.Option;
import io.github.lazzz.sagittarius.system.model.entity.SysDict;
import com.mybatisflex.core.service.IService;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysDictPageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysDictVO;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 服务层。
 *
 * @author Lazzz
 * @since 1.0
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 根据字典类型编码查询字典列表
     *
     * @param typeCode 字典类型编码
     * @return {@link List<DictDetailDTO>} 字典列表
     */
    List<DictDetailDTO> getDictListByType(@PathVariable String typeCode);

    /**
     * 字典数据项列表
     *
     * @return {@link Page<SysDictVO>} 字典数据项分页列表
     */
    List<DictDetailDTO> getDictDetailDTO();

    /**
     * 字典数据项分页列表
     *
     * @param query 查询参数
     * @return {@link Page<SysDictVO>} 字典数据项分页列表
     */
    Page<SysDictVO> getDictPage(SysDictPageQuery query);

    /**
     * 字典数据项表单
     *
     * @param id 字典数据项ID
     * @return {@link SysDictForm} 字典数据项表单
     */
    SysDictForm getDictForm(Long id);

    /**
     * 新增字典数据项
     *
     * @param dictForm 字典数据项表单
     * @return {@link Boolean}
     */
    boolean saveDict(SysDictForm dictForm);

    /**
     * 修改字典数据项
     *
     * @param id       字典数据项ID
     * @param dictForm 字典数据项表单
     * @return {@link Boolean}
     */
    boolean updateDict(Long id, SysDictForm dictForm);

    /**
     * 删除字典数据项
     *
     * @param idsStr 字典数据项ID，多个以英文逗号(,)分割
     * @return {@link Boolean}
     */
    boolean deleteDict(String idsStr);

    /**
     * 获取字典下拉列表
     *
     * @param typeCode 字典类型编码
     * @return {@link List<Option>} 字典下拉列表
     */
    List<Option<String>> listDictOptions(String typeCode);
    
}