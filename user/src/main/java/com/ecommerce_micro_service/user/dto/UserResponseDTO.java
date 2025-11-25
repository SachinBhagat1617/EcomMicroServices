package com.ecommerce_micro_service.user.dto;

import com.ecommerce_micro_service.user.models.UserRole;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
