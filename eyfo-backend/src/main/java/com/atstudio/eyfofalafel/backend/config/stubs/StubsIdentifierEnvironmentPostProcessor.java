package com.atstudio.eyfofalafel.backend.config.stubs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class StubsIdentifierEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String googleApiKey = environment.getProperty("GOOGLE_API_KEY");
        if (isBlank(googleApiKey)) {
            log.info("No google api key provided, will start with stubs profile");
            environment.setActiveProfiles(ArrayUtils.add(environment.getActiveProfiles(), "stubs"));
        }
    }
}