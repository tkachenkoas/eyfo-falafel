package com.atstudio.eyfofalafel.backend.config.common;

import com.atstudio.eyfofalafel.backend.service.location.LocationObjectFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MappersConfig implements WebMvcConfigurer {

    @Bean
    @Qualifier("nullSkippingMapper")
    public ObjectMapper nullSkippingMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Bean
    public LocationObjectFactory locationObjectFactory() {
        return new LocationObjectFactory();
    }

}
