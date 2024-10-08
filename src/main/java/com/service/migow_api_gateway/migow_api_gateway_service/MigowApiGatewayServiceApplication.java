package com.service.migow_api_gateway.migow_api_gateway_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import com.service.migow_api_gateway.migow_api_gateway_service.infra.helpers.JwtAuthenticationFilter;

@SpringBootApplication
public class MigowApiGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigowApiGatewayServiceApplication.class, args);
    }

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("hello-world", r -> r.path("/get").uri("https://httpbin.org/"))
                .route("users-service", r -> r
                .path("/u-s/**")
                .filters(f -> f.rewritePath("/u-s/(?<segment>.*)", "/api/v1/${segment}").filter(filter))
                .uri("http://localhost:8081"))
                .route("posts-service", r -> r
                .path("/p-s/**")
                .filters(f -> f.rewritePath("/p-s/(?<segment>.*)", "/api/v1/${segment}").filter(filter))
                .uri("http://localhost:8082"))
                .route("user-event-service", r -> r
                .path("/ue-s/**")
                .filters(f -> f.rewritePath("/ue-s/(?<segment>.*)", "/api/v1/${segment}").filter(filter))
                .uri("http://localhost:8083"))
                .build();
    }

}
