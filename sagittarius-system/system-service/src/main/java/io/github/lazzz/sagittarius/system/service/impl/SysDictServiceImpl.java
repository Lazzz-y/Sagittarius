package io.github.lazzz.sagittarius.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.anno.CacheUpdate;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.web.model.Option;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysDictPageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysDictVO;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.lazzz.sagittarius.system.service.ISysDictService;
import io.github.lazzz.sagittarius.system.model.entity.SysDict;
import io.github.lazzz.sagittarius.system.mapper.SysDictMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

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
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    private final Converter converter;

    @Override
    @SuppressWarnings("unchecked")
    public List<DictDetailDTO> getDictListByType(String typeCode) {
        return converter.convert(this.list(queryChain()
                .eq(SysDict::getTypeCode, typeCode)
                .select(
                        SysDict::getValue,
                        SysDict::getName,
                        SysDict::getTypeCode
                )
        ), DictDetailDTO.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DictDetailDTO> getDictDetailDTO() {
        return converter.convert(this.list(
                queryChain()
                        .select(
                                SysDict::getValue,
                                SysDict::getName,
                                SysDict::getTypeCode
                        )
        ), DictDetailDTO.class);
    }

    @Override
    public Page<SysDictVO> getDictPage(SysDictPageQuery query) {
        var wrapper = QueryWrapper.create().from(SysDict.class)
                .like(SysDict::getTypeCode, query.getTypeCode(), StrUtil.isNotBlank(query.getTypeCode()))
                .like(SysDict::getName, query.getKeywords(), StrUtil.isNotBlank(query.getKeywords()));
        Page<SysDict> page = this.mapper.paginate(query.toPage(), wrapper);
        return page.map(m -> converter.convert(m, SysDictVO.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    public SysDictForm getDictForm(Long id) {
        SysDict dict = this.getOne(queryChain()
                .eq(SysDict::getId, id)
                .select(
                        SysDict::getId,
                        SysDict::getTypeCode,
                        SysDict::getName,
                        SysDict::getValue,
                        SysDict::getStatus,
                        SysDict::getSort,
                        SysDict::getRemark));
        Assert.isTrue(dict != null, "字典数据项不存在");
        return converter.convert(dict, SysDictForm.class);
    }

    @Override
    @CacheUpdate(
            area = CacheConstants.DICT_AREA,
            name = CacheConstants.DICT_NAME,
            key = CacheConstants.SPEL_DICT_FORM_TYPE_CODE_KEY,
            value = "#result")
    @Transactional
    public List<DictDetailDTO> saveDict(SysDictForm form) {
        SysDict dict = converter.convert(form, SysDict.class);
        this.save(dict);
        return getDictListByType(dict.getTypeCode());
    }

    @Override
    @CacheUpdate(
            area = CacheConstants.DICT_AREA,
            name = CacheConstants.DICT_NAME,
            key = CacheConstants.SPEL_DICT_FORM_TYPE_CODE_KEY,
            value = "#result")
    @Transactional
    public List<DictDetailDTO> updateDict(Long id, SysDictForm form) {
        SysDict dict = converter.convert(form, SysDict.class);
        dict.setId(id);
        this.updateById(dict);
        return getDictListByType(dict.getTypeCode());
    }

    @Override
    @Transactional
    public boolean deleteDict(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除数据为空");
        List<Long> ids = Arrays.stream(idsStr.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(ids);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Option<String>> listDictOptions(String typeCode) {
        List<SysDict> dictList = this.list(queryChain()
                .eq(SysDict::getTypeCode, typeCode)
                .select(SysDict::getValue, SysDict::getName)
        );
        return CollectionUtil.emptyIfNull(dictList)
                .stream()
                .map(item -> new Option<>(item.getValue(), item.getName()))
                .toList();
    }
}