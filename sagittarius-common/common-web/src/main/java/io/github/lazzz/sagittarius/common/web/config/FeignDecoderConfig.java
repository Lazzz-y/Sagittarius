package io.github.lazzz.sagittarius.common.web.config;


import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import io.github.lazzz.sagittarius.common.web.decoder.FeignDecoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;

/**
 * Feign 解码器，处理 Result<T> 为 T
 *
 * @author Lazz
 * @date 2025/09/22 23:55
 **/
public class FeignDecoderConfig {

    @Bean
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverters> messageConverters) {
        return new OptionalDecoder(
                (new ResponseEntityDecoder
                        (new FeignDecoder(
                                new SpringDecoder(messageConverters)
                        ))
                )
        );
    }

}

