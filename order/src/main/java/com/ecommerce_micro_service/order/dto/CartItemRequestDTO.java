package com.ecommerce_micro_service.order.dto;

import lombok.Data;

@Data
public class CartItemRequestDTO {
    private Long productId;
    private Integer quantity;
}
