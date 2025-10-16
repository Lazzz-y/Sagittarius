package io.github.lazzz.sagittarius.system.controller;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.github.lazzz.sagittarius.common.web.annotation.PreventDuplicateResubmit;
import io.github.lazzz.sagittarius.common.web.model.Option;
import io.github.lazzz.sagittarius.common.constant.CacheConstants;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictForm;
import io.github.lazzz.sagittarius.system.model.request.form.SysDictTypeForm;
import io.github.lazzz.sagittarius.system.model.request.query.SysDictPageQuery;
import io.github.lazzz.sagittarius.system.model.request.query.SysDictTypePageQuery;
import io.github.lazzz.sagittarius.system.model.vo.SysDictTypeVO;
import io.github.lazzz.sagittarius.system.model.vo.SysDictVO;
import io.github.lazzz.sagittarius.system.service.ISysDictTypeService;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import lombok.RequiredArgsConstructor;
import com.mybatisflex.core.paginate.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.github.lazzz.sagittarius.system.service.ISysDictService;

import java.util.List;

import io.github.lazzz.sagittarius.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 字典接口
 *
 * @author Lazzz
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/dict")
@Tag(name = "05.字典接口")
@RequiredArgsConstructor
public class SysDictController {

    private final ISysDictService sysDictService;

    private final ISysDictTypeService sysDictTypeService;

    @Operation(summary = "根据类型编码获取字典缓存", hidden = true)
    @RequestMapping("/{typeCode}/dict")
    public Result<List<DictDetailDTO>> getDictListByType(
            @Parameter(description = "字典类型编码")
            @PathVariable String typeCode){
        return Result.success(sysDictService.getDictListByType(typeCode));
    }

    @Operation(summary = "字典列表")
    @GetMapping("/list")
    @Cached(
            name = "dict:",
            key = CacheConstants.SPEL_DICT_KEY,
            expire = 24,
            localExpire = 12,
            cacheType = CacheType.BOTH
    )
    public Result<List<DictDetailDTO>> getDictList() {
        return Result.success(sysDictService.getDictDetailDTO());
    }

    @Operation(summary = "字典分页查询")
    @GetMapping("/page")
    public Result<Page<SysDictVO>> getDictPage(
            @ParameterObject SysDictPageQuery query
    ) {
        return Result.success(sysDictService.getDictPage(query));
    }

    @Operation(summary = "字典数据表单数据")
    @GetMapping("/{id}/form")
    public Result<SysDictForm> getDictForm(
            @Parameter(description = "字典ID") @PathVariable Long id
    ) {
        SysDictForm formData = sysDictService.getDictForm(id);
        return Result.success(formData);
    }

    @Operation(summary = "新增字典")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:dict:add')")
    @PreventDuplicateResubmit
    public Result<Boolean> saveDict(
            @RequestBody SysDictForm form
    ) {
        boolean result = sysDictService.saveDict(form);
        return Result.judge(result);
    }

    @Operation(summary = "修改字典")
    @PutMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:dict:edit')")
    public Result<Boolean> updateDict(
            @PathVariable Long id,
            @RequestBody SysDictForm form
    ) {
        boolean status = sysDictService.updateDict(id, form);
        return Result.judge(status);
    }

    @Operation(summary = "删除字典")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:dict:delete')")
    public Result<Boolean> deleteDict(
            @Parameter(description = "字典ID，多个以英文逗号(,)拼接") @PathVariable String ids
    ) {
        boolean result = sysDictService.deleteDict(ids);
        return Result.judge(result);
    }


    @Operation(summary = "字典下拉列表")
    @GetMapping("/options")
    public Result<List<Option<String>>> listDictOptions(
            @Parameter(description = "字典类型编码") @RequestParam String typeCode
    ) {
        List<Option<String>> list = sysDictService.listDictOptions(typeCode);
        return Result.success(list);
    }


    /*----------------------------------------------------*/
    @Operation(summary = "字典类型分页列表")
    @GetMapping("/types/page")
    public Result<Page<SysDictTypeVO>> getDictTypePage(
            @ParameterObject SysDictTypePageQuery query
    ) {
        Page<SysDictTypeVO> result = sysDictTypeService.getDictTypePage(query);
        return Result.success(result);
    }

    @Operation(summary = "字典类型表单数据")
    @GetMapping("/types/{id}/form")
    public Result<SysDictTypeForm> getDictTypeForm(
            @Parameter(description = "字典ID") @PathVariable Long id
    ) {
        SysDictTypeForm dictTypeForm = sysDictTypeService.getDictTypeForm(id);
        return Result.success(dictTypeForm);
    }

    @Operation(summary = "新增字典类型")
    @PostMapping("/types")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:add')")
    @PreventDuplicateResubmit
    public Result<Boolean> saveDictType(@RequestBody SysDictTypeForm form) {
        boolean result = sysDictTypeService.saveDictType(form);
        return Result.judge(result);
    }

    @Operation(summary = "修改字典类型")
    @PutMapping("/types/{id}")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:edit')")
    public Result<Boolean> updateDictType(@PathVariable Long id, @RequestBody SysDictTypeForm form) {
        boolean status = sysDictTypeService.updateDictType(id, form);
        return Result.judge(status);
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/types/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:dict_type:delete')")
    public Result<Boolean> deleteDictTypes(
            @Parameter(description = "字典类型ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = sysDictTypeService.deleteDictTypes(ids);
        return Result.judge(result);
    }

    @Operation(summary = "获取字典类型的数据项")
    @GetMapping("/types/{typeCode}/items")
    public Result<List<Option<String>>> listDictTypeItems(
            @Parameter(description = "字典类型编码") @PathVariable String typeCode
    ) {
        List<Option<String>> list = sysDictTypeService.listDictItemsByTypeCode(typeCode);
        return Result.success(list);
    }

}