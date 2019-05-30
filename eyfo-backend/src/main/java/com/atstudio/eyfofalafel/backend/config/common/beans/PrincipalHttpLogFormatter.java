package com.atstudio.eyfofalafel.backend.config.common.beans;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zalando.logbook.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class PrincipalHttpLogFormatter implements HttpLogFormatter {

    private final JsonHttpLogFormatter delegate;

    public PrincipalHttpLogFormatter(final JsonHttpLogFormatter delegate) {
        this.delegate = delegate;
    }

    @Override
    public String format(final Precorrelation<HttpRequest> precorrelation) throws IOException {
        final Map<String, Object> content = delegate.prepare(precorrelation);
        content.put("principal", getPrincipal());
        return delegate.format(content);
    }

    @Override
    public String format(final Correlation<HttpRequest, HttpResponse> correlation) throws IOException {
        final Map<String, Object> content = delegate.prepare(correlation);
        content.put("principal", getPrincipal());
        return delegate.format(content);
    }

    private String getPrincipal() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication).map(Authentication::getName).orElse("anonymous");
    }

}
