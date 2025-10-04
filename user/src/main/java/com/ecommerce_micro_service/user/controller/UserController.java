package com.ecommerce_micro_service.user.controller;


import com.ecommerce_micro_service.user.dto.UserResponseDTO;
import com.ecommerce_micro_service.user.exceptions.APIException;
import com.ecommerce_micro_service.user.models.User;

import com.ecommerce_micro_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor //is a Lombok annotation used in Java to auto-generate a constructor for all final fields and fields annotated with @NonNull
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users=userService.getAllUser();
        if(users.isEmpty()){
            throw new APIException("No Users Found");
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody  UserResponseDTO userResponseDTO) {
        User user=modelMapper.map(userResponseDTO,User.class);
        User addedUser=userService.addUser(user);
        return new ResponseEntity<>("User Added Successfully",HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String id){
        User user=userService.getUserById(id);
        log.info("Request received from userid: {}",id);
        log.trace("this is trace level log - Used for very Detailed Log");
        log.error("this Error level log - This is error level log");
        log.info("This is info level log - General Information System");
        log.debug("User for development Debugging");

        UserResponseDTO userResponseDTO=modelMapper.map(user,UserResponseDTO.class);
        return new ResponseEntity<>(userResponseDTO,HttpStatus.OK) ;
    }

    @PutMapping("/users")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        userService.updateUser(user);
        return new ResponseEntity<>("User Updated Successfully",HttpStatus.OK);
    }

}
