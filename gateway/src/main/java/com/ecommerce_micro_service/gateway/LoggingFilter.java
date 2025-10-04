package com.ecommerce_micro_service.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

<<<<<<< HEAD
@Component // whole class as bean
=======
@Component
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c
public class LoggingFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Received request from path:{}",exchange.getRequest().getPath());
        return chain.filter(exchange);
    }
}
