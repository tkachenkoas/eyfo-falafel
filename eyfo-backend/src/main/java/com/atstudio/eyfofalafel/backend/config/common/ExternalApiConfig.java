package com.atstudio.eyfofalafel.backend.config.common;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalApiConfig {

    @Bean
    public GeoApiContext geoApiContext(@Value("${GOOGLE_API_KEY}") String apiKey) {
        return new GeoApiContext.Builder().apiKey(apiKey).maxRetries(3).build();
    }

}
