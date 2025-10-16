package io.github.lazzz.sagittarius.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.sagittarius.common.web.model.Option;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.system.model.entity.SysDict;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictTypeForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysDictTypePageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysDictTypeVO;
import io.github.lazzz.sagittarius.system.service.ISysDictService;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.system.service.ISysDictTypeService;
import io.github.lazzz.sagittarius.system.model.entity.SysDictType;
import io.github.lazzz.sagittarius.system.mapper.SysDictTypeMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * 服务层实现。
 *
 * @author Lazzz
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final Converter converter;

    private final ISysDictService sysDictService;

    @Override
    public Page<SysDictTypeVO> getDictTypePage(SysDictTypePageQuery query) {
        var wrapper = new QueryWrapper()
                .like(SysDictType::getName, query.getKeywords(), StrUtil.isNotBlank(query.getKeywords()))
                .or(w -> {
                    w.like(SysDictType::getCode, query.getKeywords(), StrUtil.isNotBlank(query.getKeywords()));
                });
        Page<SysDictType> page = this.mapper.paginate(query.toPage(), wrapper);
        return page.map(m -> converter.convert(m, SysDictTypeVO.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    public SysDictTypeForm getDictTypeForm(Long id) {
        SysDictType dictType = this.getOne(QueryWrapper
                .create()
                .eq(SysDictType::getId, id)
                .select(
                        SysDictType::getId,
                        SysDictType::getName,
                        SysDictType::getCode,
                        SysDictType::getStatus,
                        SysDictType::getRemark));
        Assert.isTrue(dictType != null, "字典类型不存在");
        return converter.convert(dictType, SysDictTypeForm.class);
    }

    @Override
    public boolean saveDictType(SysDictTypeForm dictTypeForm) {
        SysDictType dictType = converter.convert(dictTypeForm, SysDictType.class);
        return this.save(dictType);
    }

    @Override
    public boolean updateDictType(Long id, SysDictTypeForm dictTypeForm) {
        SysDictType sysDictType = this.getById(id);
        Assert.isTrue(sysDictType != null, "字典类型不存在");
        dictTypeForm.setId(id);
        SysDictType dictType = converter.convert(dictTypeForm, SysDictType.class);
        boolean result = this.updateById(dictType);
        if (result) {
            // 字典类型code变化，同步修改字典项的类型code
            String oldCode = sysDictType.getCode();
            String newCode = dictTypeForm.getCode();
            if (!StrUtil.equals(oldCode, newCode)) {
                sysDictService.updateChain()
                        .eq(SysDict::getTypeCode, oldCode)
                        .set(SysDict::getTypeCode, newCode);
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deleteDictTypes(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除数据为空");
        List<String> ids = Arrays.stream(idsStr.split(","))
                .toList();
        // 删除字典数据项
        List<String> dictTypeCodes = this.list(queryChain()
                        .in(SysDictType::getId, ids).select(SysDictType::getCode))
                .stream().map(SysDictType::getCode)
                .toList();
        If.withNotEmpty(dictTypeCodes,
                (list) -> sysDictService
                        .remove(queryChain()
                                .in(SysDict::getTypeCode, list)
                        )
        );
        return this.removeByIds(ids);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Option<String>> listDictItemsByTypeCode(String typeCode) {
        List<SysDict> dictItems = sysDictService.list(queryChain()
                .eq(SysDict::getTypeCode, typeCode)
                .select(SysDict::getValue, SysDict::getName)
        );
        // 转换下拉数据
        return CollectionUtil.emptyIfNull(dictItems)
                .stream()
                .map(dictItem -> new Option<>(dictItem.getValue(), dictItem.getName()))
                .toList();
    }
}