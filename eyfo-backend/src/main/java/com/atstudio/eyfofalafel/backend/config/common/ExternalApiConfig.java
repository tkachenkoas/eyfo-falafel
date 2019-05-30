package com.atstudio.eyfofalafel.backend.config.common;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ExternalApiConfig {

    @Value("${GOOGLE_API_KEY}")
    private String apiKey;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder().apiKey(apiKey).maxRetries(3).build();
    }

}
