package com.ecommerce_micro_service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemsDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
}
