package io.github.lazzz.sagittarius.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.Cache;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.common.redisson.model.LockInfo;
import io.github.lazzz.sagittarius.common.redisson.model.LockType;
import io.github.lazzz.sagittarius.common.redisson.service.LockService;
import io.github.lazzz.sagittarius.common.utils.condition.If;
import io.github.lazzz.sagittarius.common.web.model.Option;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysDictPageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysDictVO;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    private final Converter converter;

    private final Cache<String, List<DictDetailDTO>> dictCache;

    @Resource
    @Qualifier("readWriteLockService")
    private LockService lockService;

    @Override
    @SuppressWarnings("unchecked")
    public List<DictDetailDTO> getDictListByType(String typeCode) {
        var dictList = this.list(queryChain()
                .eq(SysDict::getTypeCode, typeCode)
                .select(
                        SysDict::getValue,
                        SysDict::getName,
                        SysDict::getTypeCode
                ));
        var dtoList = CollectionUtil.<DictDetailDTO>newArrayList();
        BeanUtils.copyProperties(dictList, dtoList);
        return dtoList;
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
    @Transactional
    public Boolean saveDict(SysDictForm form) {
        SysDict dict = converter.convert(form, SysDict.class);
        var rs = this.save(dict);
        If.ifThen(rs, () -> refreshDictCache(dict.getTypeCode(), dict.getId()));
        return rs;
    }

    @Override
    @Transactional
    public Boolean updateDict(Long id, SysDictForm form) {
        SysDict oldDict = this.getById(id);
        Assert.isTrue(oldDict != null, "字典数据项不存在");
        String typeCode = oldDict.getTypeCode();
        SysDict dict = converter.convert(form, SysDict.class);
        dict.setId(id);
        var rs = this.updateById(dict);
        If.ifThen(rs, () -> refreshDictCache(typeCode, id));
        return rs;
    }

    @Override
    @Transactional
    public boolean deleteDict(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除数据为空");
        List<Long> ids = Arrays.stream(idsStr.split(","))
                .map(Long::parseLong)
                .toList();
        List<SysDict> list = this.list(queryChain().in(SysDict::getId, ids));
        var rs = this.removeByIds(ids);
        If.ifThen(rs, () ->
                list.forEach(dict ->
                        refreshDictCache(dict.getTypeCode(), dict.getId()))
        );
        return rs;
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

    private void refreshDictCache(String typeCode, Long id) {
        String lockKey = CacheConstants.DICT_LOCK_PREFIX + typeCode + ":" + id;
        String cacheKey = CacheConstants.SUB_DICT_PREFIX + typeCode;
        var lockInfo = LockInfo.builder()
                .name(lockKey)
                .lockType(LockType.WRITE)
                .waitTime(5)
                .leaseTime(10)
                .build();
        try (LockService lock = lockService) {
            lock.setLockInfo(lockInfo);
            if (lock.lock()) {
                dictCache.remove(cacheKey);
                dictCache.put(cacheKey, this.getDictListByType(typeCode));
            }
        } catch (Exception e) {
            log.error("刷新字典缓存失败，typeCode: {}", typeCode, e);
        }
    }
}