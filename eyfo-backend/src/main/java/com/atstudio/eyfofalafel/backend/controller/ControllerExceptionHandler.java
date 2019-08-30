package com.atstudio.eyfofalafel.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAnyException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return status(INTERNAL_SERVER_ERROR).body(messageResp(ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFound(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return status(NOT_FOUND).body(messageResp(ex.getMessage()));
    }

    private static Map<String, String> messageResp(String message) {
        return singletonMap("message", message);
    }

}

