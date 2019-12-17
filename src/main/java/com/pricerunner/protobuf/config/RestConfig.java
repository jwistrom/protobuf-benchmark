package com.pricerunner.protobuf.config;

import com.pricerunner.common.rest.config.ServiceWebMvcConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
public class RestConfig extends ServiceWebMvcConfig {

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter());
        converters.add(new ProtobufHttpMessageConverter());
        super.configureMessageConverters(converters);
    }

}
