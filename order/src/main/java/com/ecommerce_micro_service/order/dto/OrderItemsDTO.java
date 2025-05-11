package com.ecommerce_micro_service.order.dto;

import lombok.Data;

@Data
public class OrderItemsDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
}
