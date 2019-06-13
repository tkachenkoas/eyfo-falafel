package com.atstudio.eyfofalafel.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(Exception ex) {
        return logAndBuildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return logAndBuildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<String> logAndBuildErrorResponse(Exception ex, HttpStatus status) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(status).body(ex.getMessage());
    }

}

