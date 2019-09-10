package com.atstudio.eyfofalafel.backend.config.stubs;

import com.atstudio.eyfofalafel.backend.service.location.google.GoogleApi;
import com.atstudio.eyfofalafel.backend.service.location.google.GoogleApiStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;

@Configuration
public class StubsConfig {

    @Bean
    @Profile("stubs")
    public GoogleApi stubGoogleApi(ObjectMapper mapper) {
        return new GoogleApiStub(
                getFile("stub/google-api/place-autocomplete.json"),
                getFile("stub/google-api/geocode.json"),
                getFile("stub/google-api/reverse-geocode.json"),
                mapper
        );
    }

    private File getFile(String name) {
        return new File(
                getClass().getClassLoader().getResource(name).getFile()
        );
    }
}
