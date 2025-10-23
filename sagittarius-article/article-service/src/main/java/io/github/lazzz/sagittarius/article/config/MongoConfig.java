package io.github.lazzz.sagittarius.article.config;


import org.springframework.batch.core.converter.DateToStringConverter;
import org.springframework.batch.core.converter.StringToDateConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置MongoDB
 *
 * @author Lazzz
 * @date 2025/10/22 22:11
**/
@Configuration
public class MongoConfig {

    @Bean
    public MongoMappingContext mongoMappingContext(){
        MongoMappingContext mongoMappingContext = new MongoMappingContext();
        // 使用蛇形命名策略
        mongoMappingContext.setFieldNamingStrategy(new SnakeCaseFieldNamingStrategy());
        return mongoMappingContext;
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        List<Converter<?,?>> converters = new ArrayList<>();
        // 添加自定义转换器 Date <-> String
        converters.add(new StringToDateConverter());
        converters.add(new DateToStringConverter());
        return new MongoCustomConversions(converters);
    }

}

