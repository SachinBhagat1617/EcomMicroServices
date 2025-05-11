package com.ecommerce_micro_service.order.service;

import com.ecommerce_micro_service.order.dto.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO placeOrder(String userId);
}
