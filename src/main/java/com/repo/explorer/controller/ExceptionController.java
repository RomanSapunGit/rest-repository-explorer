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

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ResponseExceptionDTO> handleNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseExceptionDTO> handleHttpClientErrorException(HttpClientErrorException ex) {
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .status(ex.getStatusCode().value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseExceptionDTO> handleException(Exception ex) {
        String errorMessage = ex.getMessage();
        ResponseExceptionDTO response = ResponseExceptionDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}

