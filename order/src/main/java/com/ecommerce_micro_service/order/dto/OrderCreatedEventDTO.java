package com.ecommerce_micro_service.order.dto;

import com.ecommerce_micro_service.order.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEventDTO {
    private Long orderId;
    private String userId;
    private OrderStatus status;
    private List<OrderItemsDTO> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
