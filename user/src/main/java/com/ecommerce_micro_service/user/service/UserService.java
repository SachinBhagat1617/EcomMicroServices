package com.ecommerce_micro_service.user.service;


import com.ecommerce_micro_service.user.dto.UserResponseDTO;
import com.ecommerce_micro_service.user.models.User;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUser();

    User addUser(User user);

    User getUserById(String id);

    void updateUser(User user);
}
