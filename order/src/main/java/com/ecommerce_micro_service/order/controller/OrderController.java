package com.ecommerce_micro_service.order.controller;

import com.ecommerce_micro_service.order.dto.OrderResponseDTO;
import com.ecommerce_micro_service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(
            @RequestHeader("X-User-ID") String userId
    ){
        OrderResponseDTO orderResponseDTO=orderService.placeOrder(userId);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
