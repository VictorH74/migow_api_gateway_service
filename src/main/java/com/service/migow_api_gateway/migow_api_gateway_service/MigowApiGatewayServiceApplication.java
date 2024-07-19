package com.service.migow_api_gateway.migow_api_gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MigowApiGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MigowApiGatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("hello-world", r -> r.path("/get").uri("https://httpbin.org/"))
				.route("users-service", r -> r
						.path("/u-s/**")
						.filters(f -> f.rewritePath("/u-s/(?<segment>.*)", "/api/v1/${segment}"))
						.uri("http://localhost:8081"))
				.route("posts-service", r -> r
						.path("/p-s/**")
						.filters(f -> f.rewritePath("/p-s/(?<segment>.*)", "/api/v1/${segment}"))
						.uri("http://localhost:8082"))
				.route("users-service", r -> r
						.path("/ue-s/**")
						.filters(f -> f.rewritePath("/ue-s/(?<segment>.*)", "/api/v1/${segment}"))
						.uri("http://localhost:8083"))
				// .route("users-service", r -> r.path("/users").filters(f ->
				// f.rewritePath("/resource-server/users/(?<segment>.*)",
				// "/${segment}")).uri("http://localhost:8081/"))
				// .route("path_route", r -> r.path("/get")
				// .uri("http://httpbin.org"))
				// .route("host_route", r -> r.host("*.myhost.org")
				// .uri("http://httpbin.org"))
				// .route("rewrite_route", r -> r.host("*.rewrite.org")
				// .filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
				// .uri("http://httpbin.org"))
				.build();
	}

}
