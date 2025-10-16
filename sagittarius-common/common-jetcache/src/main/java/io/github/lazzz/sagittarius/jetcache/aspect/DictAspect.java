package io.github.lazzz.sagittarius.jetcache.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import io.github.lazzz.sagittarius.common.constant.SystemConstants;
import io.github.lazzz.sagittarius.common.result.Result;
import io.github.lazzz.sagittarius.jetcache.annotation.Dict;
import io.github.lazzz.sagittarius.jetcache.service.DictCacheService;
import io.github.lazzz.sagittarius.system.dto.DictDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典自动转换切面（专为MyBatis-Flex Page对象优化）
 * 拦截Controller返回结果，自动处理@Dict注解标记的字段
 *
 * @author Lazzz
 * @date 2025/10/16 21:31
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DictAspect {

    private final DictCacheService dictCacheService;

    @Pointcut("execution(* io.github.lazzz.sagittarius..controller..*(..))")
    public void dictPointCut() {
    }

    /**
     * 后置处理：将返回结果转换为动态结构并添加字典翻译字段
     */
    @AfterReturning(pointcut = "dictPointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Result<?> result) {
        if (result == null || result.getData() == null) {
            log.debug("Result或data为null，跳过字典转换");
            return;
        }
        log.debug("开始处理字典转换，方法：{}", joinPoint.getSignature().getName());
        // 处理数据并替换原data
        Object handledData = handleData(result.getData());
        result.setDataWithCast(handledData);
    }

    /**
     * 递归处理数据，返回含翻译字段的动态结构
     */
    private Object handleData(Object data) {
        if (data == null) {
            return null;
        }

        // 1. 处理MyBatis-Flex分页对象
        if (data instanceof Page) {
            return handleMyBatisFlexPage((Page<?>) data);
        }

        // 2. 处理集合
        if (data instanceof List<?>) {
            return ((List<?>) data).stream()
                    .map(this::handleData)
                    .collect(Collectors.toList());
        }

        // 3. 处理数组
        if (data.getClass().isArray()) {
            Object[] array = (Object[]) data;
            return Arrays.stream(array)
                    .map(this::handleData)
                    .toArray();
        }

        // 4. 处理单个对象（基础类型/JDK类直接返回，自定义类转换为JSONObject）
        Class<?> clazz = data.getClass();
        if (clazz.isPrimitive() || clazz.getName().startsWith("java.") || clazz.getName().startsWith("javax.")) {
            return data;
        }

        // 转换为含翻译字段的JSONObject
        return handleObject(data, clazz);
    }

    /**
     * 处理分页对象，转换records为含翻译字段的结构
     */
    private Object handleMyBatisFlexPage(Page<?> page) {
        List<?> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return page;
        }
        // 转换分页数据列表
        List<?> handledRecords = records.stream()
                .map(this::handleData)
                .toList();
        // 复制原分页属性，替换records
        Page<Object> handledPage = new Page<>();
        handledPage.setPageNumber(page.getPageNumber());
        handledPage.setPageSize(page.getPageSize());
        handledPage.setTotalRow(page.getTotalRow());
        handledPage.setTotalPage(page.getTotalPage());
        handledPage.setOptimizeCountQuery(page.needOptimizeCountQuery());
        handledPage.setRecords(Arrays.asList(handledRecords.toArray()));
        return handledPage;
    }

    /**
     * 处理单个对象，返回含原字段和翻译字段的JSONObject
     */
    @SneakyThrows
    private Object handleObject(Object data, Class<?> clazz) {
        // 用JSONObject存储原字段和翻译后的字段
        JSONObject dataJson = new JSONObject();

        // 1. 复制原对象所有字段到JSONObject
        Field[] allFields = ReflectUtil.getFields(clazz);
        for (Field field : allFields) {
            field.setAccessible(true);
            dataJson.put(field.getName(), field.get(data));
        }

        // 2. 处理@Dict注解字段，添加翻译后的字段到JSONObject
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            Dict dict = field.getAnnotation(Dict.class);
            if (dict == null) {
                continue;
            }

            // 解析注解参数
            String typeCode = dict.typeCode();
            String sourceFieldName = StrUtil.isBlank(dict.sourceField()) ? field.getName() : dict.sourceField();
            String targetFieldName = StrUtil.isBlank(dict.targetField()) ? sourceFieldName + SystemConstants.DICT_VALUE_SUFFIX : dict.targetField();

            // 获取源字段（字典编码）的值
            Field sourceField = ReflectUtil.getField(clazz, sourceFieldName);
            if (sourceField == null) {
                log.warn("类{}中未找到源字段{}，跳过字典转换", clazz.getName(), sourceFieldName);
                continue;
            }
            sourceField.setAccessible(true);
            Object sourceValueObj = sourceField.get(data);
            String sourceValue = sourceValueObj != null ? sourceValueObj.toString() : null;
            if (sourceValue == null) {
                continue;
            }

            // 从缓存获取字典数据
            List<DictDetailDTO> dictList = dictCacheService.getDictByType(typeCode);
            if (CollUtil.isEmpty(dictList)) {
                log.warn("字典类型{}未找到对应数据，跳过转换", typeCode);
                continue;
            }

            // 构建字典编码->名称的映射
            Map<String, String> dictMap = dictList.stream()
                    .collect(Collectors.toMap(DictDetailDTO::getValue, DictDetailDTO::getName));

            // 向JSONObject中添加翻译后的目标字段（无需实体类定义）
            String targetValue = dictMap.getOrDefault(sourceValue, sourceValue);
            dataJson.put(targetFieldName, targetValue);
            log.info("数据字典翻译: 字典类型：{}，当前翻译值：{}，翻译结果：{}", typeCode, sourceValue, targetValue);
        }

        // 3. 递归处理父类字段（合并父类字段到当前JSONObject）
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            JSONObject superDataJson = (JSONObject) handleObject(data, superClass);
            dataJson.putAll(superDataJson);
        }

        return dataJson;
    }
}