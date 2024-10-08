package com.service.migow_api_gateway.migow_api_gateway_service.domain.exceptions;

public class JwtTokenMissingException extends RuntimeException {

    public JwtTokenMissingException(String msg) {
        super(msg);
    }
}
