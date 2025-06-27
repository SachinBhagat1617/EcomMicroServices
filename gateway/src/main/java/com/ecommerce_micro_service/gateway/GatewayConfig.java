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
                                .uri("lb://product"))
                .route("user-service",r->r
                        .path("/api/users/**")
                        .uri("lb://user")
                )
                .route("order-service",r->r
                        .path("/api/orders/**","/api/cart/**")
                        .uri("lb://order")
                )
                .route("eureka-server",r->r
                        .path("/eureka/main")
                        .filters(f->f.setPath("/"))
                        .uri("http://localhost:8761")
                )
                .route("eureka-server-static",r->r
                        .path("/eureka/**")
                        .uri("http://localhost:8761")
                )
                .build();
    }
}
