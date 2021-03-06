package com.atstudio.eyfofalafel.backend.config.common;

import com.atstudio.eyfofalafel.backend.service.files.LocalStorageFileService;
import com.atstudio.eyfofalafel.backend.service.location.google.GoogleApi;
import com.atstudio.eyfofalafel.backend.service.location.google.StaticWrapperGoogleApiImpl;
import com.google.maps.GeoApiContext;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Services {

    @Bean
    public static LocalStorageFileService localStorageFileService(
            @Value("${files.drive.folder}") String fileStorageLocation
    ) {
        return new LocalStorageFileService(fileStorageLocation);
    }

    @Bean
    @ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotBlank('${GOOGLE_API_KEY}')")
    public GoogleApi remoteGoogleApi(@Value("${GOOGLE_API_KEY}") String apiKey) {
        return new StaticWrapperGoogleApiImpl(
                new GeoApiContext.Builder().apiKey(apiKey).maxRetries(3).build()
        );
    }

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

}
