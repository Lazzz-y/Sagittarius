package io.github.lazzz.sagittarius.jetcache.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 字典自动转换切面
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

    // 反射字段缓存：缓存类对应的所有字段（包括父类），避免重复反射获取
    private final Map<Class<?>, Field[]> classFieldsCache = new ConcurrentHashMap<>();

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

        // 从缓存获取当前类所有字段（包括父类）
        Field[] allFields = getCachedFields(clazz);

        // 复制原对象所有字段到JSONObject（处理@JsonFormat注解）
        for (Field field : allFields) {
            Object fieldValue = field.get(data);
            // 检查是否有@JsonFormat注解，如有则格式化日期
            JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
            if (jsonFormat != null && fieldValue != null) {
                String formattedValue = formatDateWithJsonFormat(fieldValue, jsonFormat);
                dataJson.put(field.getName(), formattedValue);
            } else {
                dataJson.put(field.getName(), fieldValue);
            }
        }


        // 2. 处理@Dict注解字段，添加翻译后的字段到JSONObject
        for (Field field : allFields) {
            Dict dict = field.getAnnotation(Dict.class);
            if (dict == null) {
                continue;
            }

            // 解析注解参数
            String typeCode = dict.typeCode();
            String sourceFieldName = StrUtil.isBlank(dict.sourceField()) ? field.getName() : dict.sourceField();
            String targetFieldName = StrUtil.isBlank(dict.targetField()) ? sourceFieldName + SystemConstants.DICT_VALUE_SUFFIX : dict.targetField();

            // 从缓存字段中查找源字段
            Field sourceField = findField(allFields, sourceFieldName);
            if (sourceField == null) {
                log.warn("类{}中未找到源字段{}，跳过字典转换", clazz.getName(), sourceFieldName);
                continue;
            }

            // 获取源字段（字典编码）的值
            Object sourceValueObj = sourceField.get(data);
            String sourceValue = sourceValueObj != null ? sourceValueObj.toString() : null;
            if (sourceValue == null) {
                continue;
            }

            // 从JetCache获取字典数据（依赖DictCacheService实现缓存逻辑）
            List<DictDetailDTO> dictList = dictCacheService.getDictByType(typeCode);
            if (CollUtil.isEmpty(dictList)) {
                log.warn("字典类型{}未找到对应数据，跳过转换", typeCode);
                continue;
            }

            // 构建字典编码->名称的映射
            Map<String, String> dictMap = dictList.stream()
                    .collect(Collectors.toMap(DictDetailDTO::getValue, DictDetailDTO::getName, (k1, k2) -> k2));

            // 向JSONObject中添加翻译后的目标字段
            String targetValue = dictMap.getOrDefault(sourceValue, sourceValue);
            dataJson.put(targetFieldName, targetValue);
            log.debug("数据字典翻译: 字典类型：{}，当前翻译值：{}，翻译结果：{}", typeCode, sourceValue, targetValue);
        }

        return dataJson;
    }

    /**
     * 从缓存获取类的所有字段（包括父类），并提前设置可访问性
     */
    private Field[] getCachedFields(Class<?> clazz) {
        return classFieldsCache.computeIfAbsent(clazz, key -> {
            List<Field> fieldList = new ArrayList<>();
            Class<?> currentClass = key;
            // 递归获取所有父类的字段（直到Object类）
            while (currentClass != null && !currentClass.equals(Object.class)) {
                Field[] fields = currentClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true); // 提前设置可访问，避免重复设置
                    fieldList.add(field);
                }
                currentClass = currentClass.getSuperclass();
            }
            return fieldList.toArray(new Field[0]);
        });
    }

    /**
     * 根据@JsonFormat注解格式化日期字段
     * 支持java.util.Date、java.time.LocalDateTime、java.time.LocalDate等类型
     */
    private String formatDateWithJsonFormat(Object dateObj, JsonFormat jsonFormat) {
        try {
            String pattern = jsonFormat.pattern();
            if (dateObj instanceof Date) {
                // 处理java.util.Date
                DateFormat dateFormat = new SimpleDateFormat(pattern);
                return dateFormat.format((Date) dateObj);
            } else if (dateObj instanceof LocalDateTime) {
                // 处理java.time.LocalDateTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return formatter.format((LocalDateTime) dateObj);
            } else {
                // 非日期类型，直接返回原值字符串
                return dateObj.toString();
            }
        } catch (Exception e) {
            log.warn("日期格式化失败（@JsonFormat处理异常），原值：{}，异常：{}", dateObj, e.getMessage());
            // 格式化失败时返回原值
            return dateObj.toString();
        }
    }

    /**
     * 从字段数组中查找指定名称的字段
     */
    private Field findField(Field[] fields, String fieldName) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }
}