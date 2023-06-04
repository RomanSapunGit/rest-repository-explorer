package com.repo.explorer.controller;

import com.repo.explorer.dto.ResponseExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ResponseExceptionDTO> handleNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseExceptionDTO(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()));
    }

    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public ResponseEntity<ResponseExceptionDTO> handleNotFoundException(WebClientResponseException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseExceptionDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseExceptionDTO> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}

