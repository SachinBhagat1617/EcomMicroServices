package com.ecommerce_micro_service.user.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    //private String id;
    private String name;
    private String email;
    private String phone;
    //private UserRole role;
    private AddressDTO address;
}
