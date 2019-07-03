package com.atstudio.eyfofalafel.backend.config.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class FileStorageRouterConfig extends WebMvcConfigurationSupport {

    @Value("${files.folder.storage}")
    private String fileStoragePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**")
                .addResourceLocations(fileStoragePath);
    }


}
