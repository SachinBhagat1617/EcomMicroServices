package com.ecommerce_micro_service.order.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
=======
import lombok.Data;

@Data
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c
public class OrderItemsDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
}
