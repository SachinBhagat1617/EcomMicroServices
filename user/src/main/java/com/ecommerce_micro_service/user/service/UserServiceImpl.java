package com.ecommerce_micro_service.user.service;

import com.ecommerce_micro_service.user.dto.UserRequest;
import com.ecommerce_micro_service.user.models.Address;
import com.ecommerce_micro_service.user.repositories.UserRepository;
import com.ecommerce_micro_service.user.dto.UserResponseDTO;
import com.ecommerce_micro_service.user.exceptions.APIException;
import com.ecommerce_micro_service.user.exceptions.ResourceNotFoundException;
import com.ecommerce_micro_service.user.models.User;
import com.ecommerce_micro_service.user.models.UserRole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    private final KeyCloakAdminService keyCloakAdminService;


    @Override
    public List<User> getAllUser() {
        List<User>users=userRepository.findAll();
        if(users.isEmpty()){
            throw new APIException("No Users Found");
        }

        return users;
    }

    @Override
    public User addUser( UserRequest userRequest) {
        String token= keyCloakAdminService.getAdminAccessToken();
        String keyCloakUSerId=
                keyCloakAdminService.createUser(token,userRequest);
        keyCloakAdminService.assignClientRoleToUser(userRequest.getUsername(),"USER",keyCloakUSerId);
        User user=new User();
        updateUserFromRequest(user,userRequest);
        user.setKeycloakId(keyCloakUSerId);
        User savedUser=userRepository.save(user);
        return savedUser;
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
        if(existingUser.getFirstName()==null){
            throw new APIException("Please provide updated details");
        }if(existingUser.equals(user)){
            throw new APIException("No changes in user details");
        }
        if(user.getFirstName()!=null){
            existingUser.setFirstName(user.getFirstName());
        }
        if(user.getLastName()!=null){
            existingUser.setLastName(user.getLastName());
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
    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);
        }
    }

}
