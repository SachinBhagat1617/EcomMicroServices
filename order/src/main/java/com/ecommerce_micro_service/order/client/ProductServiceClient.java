package com.ecommerce_micro_service.order.client;

import com.ecommerce_micro_service.order.dto.ProductResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductServiceClient {
    @GetExchange("/api/products/{id}")
    ProductResponseDTO getProductById(@PathVariable Long id);
}
