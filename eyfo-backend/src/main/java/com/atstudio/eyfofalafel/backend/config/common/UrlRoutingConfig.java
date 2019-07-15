package com.atstudio.eyfofalafel.backend.config.common;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Configuration
public class UrlRoutingConfig extends WebMvcConfigurationSupport {

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new ApiAwareRequestMappingHandlerMapping();
    }

    private static class ApiAwareRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

        @Override
        protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
            if (AnnotationUtils.findAnnotation(method.getDeclaringClass(), RestController.class) != null) {
                PatternsRequestCondition apiPattern = new PatternsRequestCondition("api").combine(mapping.getPatternsCondition());

                mapping = new RequestMappingInfo(mapping.getName(), apiPattern, mapping.getMethodsCondition(),
                        mapping.getParamsCondition(), mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                        mapping.getProducesCondition(), mapping.getCustomCondition());
            }
            super.registerHandlerMethod(handler, method, mapping);
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        // Single directory level - no need to exclude "api"
        registry.addViewController("/{x:[\\w\\-]+}").setViewName("forward:/index.html");
        // Multi-level directory path, need to exclude "api/public" on the first part of the path
        registry.addViewController("/{x:^(?!api$|public$).*$}/**/{y:[\\w\\d\\_\\-\\.]+}").setViewName("forward:/index.html");
    }

    @Value("${files.drive.folder}")
    private String fileStoragePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // swagger endpoints
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/public/**")
                .addResourceLocations(resourceLocationPrefix() + fileStoragePath.toLowerCase());

    }

    private String resourceLocationPrefix() {
        return SystemUtils.IS_OS_WINDOWS ? "file:///" : "file:";
    }

}
