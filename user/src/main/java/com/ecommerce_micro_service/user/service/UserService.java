package com.ecommerce_micro_service.user.service;


import com.ecommerce_micro_service.user.dto.UserRequest;
import com.ecommerce_micro_service.user.dto.UserResponseDTO;
import com.ecommerce_micro_service.user.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();


    User addUser(UserRequest user);

    User getUserById(String id);

    void updateUser(User user);
}
