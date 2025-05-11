package com.ecommerce_micro_service.order.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
