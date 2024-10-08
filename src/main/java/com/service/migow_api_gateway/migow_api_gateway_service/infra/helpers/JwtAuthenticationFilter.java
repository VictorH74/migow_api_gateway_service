package com.service.migow_api_gateway.migow_api_gateway_service.infra.helpers;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.service.migow_api_gateway.migow_api_gateway_service.application.services.JwtService;
import com.service.migow_api_gateway.migow_api_gateway_service.domain.exceptions.JwtTokenMalformedException;
import com.service.migow_api_gateway.migow_api_gateway_service.domain.exceptions.JwtTokenMissingException;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("JwtAuthenticationFilter.filter...");
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

        final List<String> apiEndpoints = List.of("/register", "/login");

        log.info(request.getURI().getPath());

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            log.info("retrieving token...");
            final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
            log.info("retrieved token: " + token);

            try {
                if (!jwtService.validateToken(token)) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);

                    return response.setComplete();
                }
            } catch (JwtTokenMalformedException | JwtTokenMissingException ex) {
                throw ex;
            }

            Claims claims = jwtService.extractAllClaims(token);
            exchange.getRequest().mutate().header("userId", claims.getSubject()).build();
        }

        return chain.filter(exchange);
    }

}
