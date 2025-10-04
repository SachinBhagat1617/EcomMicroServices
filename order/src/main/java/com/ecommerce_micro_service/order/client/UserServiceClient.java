package com.ecommerce_micro_service.order.client;

import com.ecommerce_micro_service.order.dto.UserResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserServiceClient {
    @GetExchange("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable String id);
}
