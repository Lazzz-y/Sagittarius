package io.github.lazzz.sagittarius.jetcache.config;


import com.alicp.jetcache.anno.SerialPolicy;
import com.alicp.jetcache.anno.support.DefaultEncoderParser;
import com.alicp.jetcache.support.DecoderMap;
import com.alicp.jetcache.support.Fastjson2ValueDecoder;
import com.alicp.jetcache.support.Fastjson2ValueEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Json编码解析器
 *
 * @author Lazzz
 * @date 2025/10/14 20:47
**/
@Component
public class JsonEncoderParser extends DefaultEncoderParser {

    public static final String SERIAL_POLICY_FASTJSON2 = "FASTJSON2";

    @Override
    public Function<Object, byte[]> parseEncoder(String valueEncoder) {

        if (SERIAL_POLICY_FASTJSON2.equalsIgnoreCase(valueEncoder)) {
            return new Fastjson2ValueEncoder(true);
        } else {
            return super.parseEncoder(valueEncoder);
        }
    }

    @Override
    public Function<byte[], Object> parseDecoder(String valueDecoder) {
        if (SERIAL_POLICY_FASTJSON2.equalsIgnoreCase(valueDecoder)) {
            Fastjson2ValueDecoder fastjson2ValueDecoder = new Fastjson2ValueDecoder(true);
            DecoderMap decoderMap = DecoderMap.defaultInstance();
            decoderMap.register(SerialPolicy.IDENTITY_NUMBER_FASTJSON2, Fastjson2ValueDecoder.INSTANCE);
            fastjson2ValueDecoder.setDecoderMap(decoderMap);
            return fastjson2ValueDecoder;
        } else {
            return super.parseDecoder(valueDecoder);
        }
    }
}

