package com.atstudio.eyfofalafel.backend.config.common;

import com.google.maps.GeoApiContext;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoConfig {

    @Bean
    public GeoApiContext geoApiContext(@Value("${GOOGLE_API_KEY}") String apiKey) {
        return new GeoApiContext.Builder().apiKey(apiKey).maxRetries(3).build();
    }

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

}
