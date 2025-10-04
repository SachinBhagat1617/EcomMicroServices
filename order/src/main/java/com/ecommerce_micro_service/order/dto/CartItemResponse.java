package com.ecommerce_micro_service.order.dto;


import lombok.Data;

@Data
public class CartItemResponse
{
    private Long id;
    private String userId;
    private Long productId;
    private Double price;
    private Integer quantity;
}
