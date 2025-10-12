package io.github.lazzz.sagittarius.common.mybatis.handler;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

/**
 * String类型转换json
 *
 * @author Lazzz
 * @date 2025/09/23 20:52
 **/
@Slf4j
@Component
@MappedTypes(value = {String.class})
@MappedJdbcTypes(value = {JdbcType.OTHER}, includeNullJdbcType = true)
public class StringObjectJsonTypeHandler extends ArrayObjectJsonTypeHandler<String> {

    public StringObjectJsonTypeHandler() {
        super(String[].class);
    }

}

