package com.ecommerce_micro_service.product.dto;

import lombok.Data;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String category;
    private Boolean active;
    private String imageUrl;
}

