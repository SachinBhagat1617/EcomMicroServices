package com.ecommerce_micro_service.user.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String Password;
    private String email;
    private String phone;
    private AddressDTO address;
}
