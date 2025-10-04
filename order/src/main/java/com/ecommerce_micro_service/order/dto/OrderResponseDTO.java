package com.ecommerce_micro_service.order.dto;


import com.ecommerce_micro_service.order.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemsDTO> orderItems;
    private LocalDateTime createdAt;
}
