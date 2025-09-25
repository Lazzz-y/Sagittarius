package io.github.lazzz.common.web.config;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.List;

/**
 * MVC 配置
 *
 * @author Lazzz 
 * @date 2025/09/25 15:16
**/
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置HTTP消息转换器，用于处理JSON数据的序列化和反序列化
     * 主要解决Long类型和BigInteger类型在前端JavaScript中精度丢失的问题
     *
     * @param converters 消息转换器列表，用于添加自定义的转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        // 创建一个MappingJackson2HttpMessageConverter实例
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        // 获取ObjectMapper实例
        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjectMapper();
        // 允许JSON字段名没有引号
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 创建一个SimpleModule实例
        SimpleModule simpleModule = new SimpleModule();
        // 后台Long值传递给前端精度丢失问题（JS最大精度整数是Math.pow(2,53)）
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        // 注册SimpleModule
        objectMapper.registerModule(simpleModule);
        // 设置ObjectMapper实例
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        // 添加转换器
        converters.add(jackson2HttpMessageConverter);
    }

}

