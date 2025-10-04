package com.ecommerce_micro_service.order.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
    private String message;
    private Boolean status;
}
