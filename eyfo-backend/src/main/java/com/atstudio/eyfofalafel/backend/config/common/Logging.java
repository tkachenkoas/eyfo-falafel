package com.atstudio.eyfofalafel.backend.config.common;

import com.atstudio.eyfofalafel.backend.config.common.beans.PrincipalHttpLogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.JsonHttpLogFormatter;

@Configuration
public class Logging {

    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass());
    }

    @Bean
    public HttpLogFormatter httpLogFormatter(final ObjectMapper mapper) {
        return new PrincipalHttpLogFormatter(new JsonHttpLogFormatter(mapper));
    }

}
