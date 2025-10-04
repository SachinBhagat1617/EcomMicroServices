package com.ecommerce_micro_service.user.service;

import com.ecommerce_micro_service.user.repositories.UserRepository;
import com.ecommerce_micro_service.user.dto.UserResponseDTO;
import com.ecommerce_micro_service.user.exceptions.APIException;
import com.ecommerce_micro_service.user.exceptions.ResourceNotFoundException;
import com.ecommerce_micro_service.user.models.User;
import com.ecommerce_micro_service.user.models.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserResponseDTO> getAllUser() {
        List<User>users=userRepository.findAll();
        if(users.isEmpty()){
            throw new APIException("No Users Found");
        }
        List<UserResponseDTO> userResponseDTOList=users.stream()
                .map(user->modelMapper.map(user,UserResponseDTO.class)).toList();
        return userResponseDTOList;
    }

    @Override
    public User addUser( User user) {

        if(userRepository.existsByEmail(user.getEmail())){
            throw new APIException("User already exists");
        }
        if (user.getRole() == null) {
            user.setRole(UserRole.CUSTOMER);
        }
        User addedUser=userRepository.save(user);

        return addedUser;
    }

    @Override
    public User getUserById(String id) {
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","id",id));
        return user;
    }

    @Override
    public void updateUser(User user) {
        String id=user.getId();
        User existingUser=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","id",id));
        if(existingUser.getName()==null){
            throw new APIException("Please provide updated details");
        }if(existingUser.equals(user)){
            throw new APIException("No changes in user details");
        }
        if(user.getName()!=null){
            existingUser.setName(user.getName());
        }
        if(user.getEmail()!=null){
            existingUser.setEmail(user.getEmail());
        }
        if(user.getPhone()!=null){
            existingUser.setPhone(user.getPhone());
        }
        if(user.getRole()!=null){
            existingUser.setRole(user.getRole());
        }
        if(user.getAddress()!=null){
            existingUser.setAddress(user.getAddress());
        }
        userRepository.save(existingUser);
    }

}
