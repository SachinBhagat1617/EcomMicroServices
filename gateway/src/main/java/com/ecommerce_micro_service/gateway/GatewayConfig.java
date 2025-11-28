package com.ecommerce_micro_service.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("product-service",r-> r
                                .path("/api/products/**")
                                .filters(f->f.circuitBreaker(config -> config
                                    .setName("ecomBreaker")
                                    .setFallbackUri("forward:/fallback/products")
//                                "forward:/fallback/products"	Internal forward to a Spring Boot controller (✅ Recommended)
//                                "/fallback/products"	Treated as an external HTTP call (⛔ May fail or cause infinite loop)
                        ))
                        .uri("lb://PRODUCT"))
                .route("user-service",r->r
                        .path("/api/users/**")
                        .uri("lb://USER")
                )
                .route("order-service",r->r
                        .path("/api/orders/**","/api/cart/**")
                        .filters(f->f.circuitBreaker(config -> config
                                .setName("ecomBreaker")
                                .setFallbackUri("forward:/fallback/orders")))
                        .uri("lb://ORDER")
                )
                .route("eureka-server",r->r
                        .path("/eureka/main")
                        .filters(f->f
                                .setPath("/")
                                .circuitBreaker(config -> config
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/eureka-server"))
                        )
                        .uri("http://eureka:8761")
                )
                .route("eureka-server-static",r->r
                        .path("/eureka/**")
                        .uri("http://eureka:8761")
                )
                .build();
    }
}

