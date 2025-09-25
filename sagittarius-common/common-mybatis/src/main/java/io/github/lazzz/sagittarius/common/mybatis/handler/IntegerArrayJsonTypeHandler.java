package io.github.lazzz.sagittarius.common.mybatis.handler;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

/**
 * Integer[] 数组类型转换
 * @author Lazzz 
 * @date 2025/09/23 20:46
**/
@Slf4j
@Component
@MappedTypes(value = {Integer[].class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class IntegerArrayJsonTypeHandler extends ArrayObjectJsonTypeHandler<Integer>{

    public IntegerArrayJsonTypeHandler() {
        super(Integer[].class);
    }

}

