package com.service.migow_api_gateway.migow_api_gateway_service.infra.http.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.service.migow_api_gateway.migow_api_gateway_service.domain.exceptions.JwtTokenMalformedException;
import com.service.migow_api_gateway.migow_api_gateway_service.domain.exceptions.JwtTokenMissingException;

@ControllerAdvice
public class JwtExceptionHandlers {

    @ExceptionHandler(JwtTokenMalformedException.class)
    public ResponseEntity<ResponseErrorBody> handlerJwtException(JwtTokenMalformedException e) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status)
                .body(new ResponseErrorBody(e.getMessage(), status.value()));
    }

    @ExceptionHandler(JwtTokenMissingException.class)
    public ResponseEntity<ResponseErrorBody> handlerJwtException(JwtTokenMissingException e) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status)
                .body(new ResponseErrorBody(e.getMessage(), status.value()));
    }
}
