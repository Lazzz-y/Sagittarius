package io.github.lazzz.sagittarius.article.config;


import org.springframework.batch.core.converter.DateToStringConverter;
import org.springframework.batch.core.converter.StringToDateConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
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

    // 配置MongoDB事务管理器 FIXME 开启事物支持需要部署副本集
//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }

}

