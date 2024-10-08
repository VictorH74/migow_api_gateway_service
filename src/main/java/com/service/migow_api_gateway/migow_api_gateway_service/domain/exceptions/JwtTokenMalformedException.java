package com.service.migow_api_gateway.migow_api_gateway_service.domain.exceptions;

public class JwtTokenMalformedException extends RuntimeException {

    public JwtTokenMalformedException(String msg) {
        super(msg);
    }

    public JwtTokenMalformedException() {
        super("Token mal formed!");
    }
}
