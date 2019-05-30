package com.atstudio.eyfofalafel.backend.config.common;

import com.atstudio.eyfofalafel.backend.service.location.LocationObjectFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MappersConfig {

    @Bean
    @Qualifier("pretty")
    public Gson prettyGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    @Primary
    public Gson defaultGson() {
        return new Gson();
    }

    @Bean
    public LocationObjectFactory locationObjectFactory() {
        return new LocationObjectFactory();
    }

}
