package com.atstudio.eyfofalafel.backend.config.security.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class RestAuthEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        if (request.getRequestURI().contains("swagger")) {
            response.addHeader("WWW-Authenticate", "Basic");
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        log.warn("Unauthorized access attempt for url " + request.getRequestURI());

        response.getOutputStream().println(
                objectMapper.writeValueAsString(
                        new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED)
                )
        );
    }

}
