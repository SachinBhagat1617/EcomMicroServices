package com.ecommerce_micro_service.order.service;

import com.ecommerce_micro_service.order.client.ProductServiceClient;
import com.ecommerce_micro_service.order.client.UserServiceClient;
import com.ecommerce_micro_service.order.dto.ProductResponseDTO;
import com.ecommerce_micro_service.order.dto.UserResponseDTO;
import com.ecommerce_micro_service.order.exceptions.ResourceNotFoundException;
import com.ecommerce_micro_service.order.repositories.CartItemRepository;
//import com.app.ecom.Repositories.ProductRepository;
//import com.app.ecom.Repositories.UserRepository;
import com.ecommerce_micro_service.order.dto.CartItemRequestDTO;
import com.ecommerce_micro_service.order.dto.CartItemResponse;
import com.ecommerce_micro_service.order.exceptions.APIException;
import com.ecommerce_micro_service.order.models.CartItem;
//import com.ecommerce_micro_service.order.models.Products;
//import com.ecommerce_micro_service.order.models.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import io.github.resilience4j.retry.annotation.Retry;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private int attempt=0;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private UserServiceClient userServiceClient;
    @Override
    @Transactional

    //@CircuitBreaker(name="productService", fallbackMethod = "fallBackAddToCart")
    @Retry(name="addToCartRetry") // this is the way to implement but there are some error look into it
    @CircuitBreaker(name="productService", fallbackMethod = "fallBackAddToCart")
    public CartItemResponse addToCart(String userId, CartItemRequestDTO request) {

        UserResponseDTO userResponse=userServiceClient.getUserById(userId);
        System.out.println(userResponse.toString());
        if (userResponse == null ||
                userResponse.getId() == null ||
                userResponse.getName() == null ||
                userResponse.getEmail() == null) {
            throw new APIException("Invalid user response received from UserService");
        }
//        Optional<User> user=userRepository.findById(Long.parseLong(userId));
//        if(user.isEmpty()){
//            throw new APIException("User not found");
//        }
//        User existingUser=user.get();
        ProductResponseDTO productResponseDTO=productServiceClient.getProductById(request.getProductId());
        if(productResponseDTO==null){
            throw new ResourceNotFoundException("Product","productId",request.getProductId());
        }
        if(!productResponseDTO.getActive()){
            throw new APIException("Product is no more available");
        }
        if(productResponseDTO.getStockQuantity()< request.getQuantity()){
            throw new APIException("Product not in Stock");
        }
        Optional<CartItem> cartItem=cartItemRepository.findByProductIdAndUserId(request.getProductId(), userId);
        //CartItem existingCartItem=cartItem.get();
        if(cartItem.isPresent()){
            //update
            CartItem existingCartItem = cartItem.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity()+request.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice()+(productResponseDTO.getPrice()*request.getQuantity()));
            cartItemRepository.save(existingCartItem);
            CartItemResponse cartIemResponse=modelMapper.map(existingCartItem,CartItemResponse.class);
            return cartIemResponse;
        }
        // create new cartItem
        CartItem newCartItem = new CartItem();
        newCartItem.setProductId(request.getProductId());
        newCartItem.setUserId(userId);
        newCartItem.setQuantity(request.getQuantity());
        newCartItem.setPrice(productResponseDTO.getPrice() * request.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        return modelMapper.map(savedCartItem, CartItemResponse.class);
    }
    public CartItemResponse fallBackAddToCart(String userId, CartItemRequestDTO request, Exception e){ // fallbackMethod should have same method to addToCart
        e.printStackTrace();
        System.out.println("Fallback Called");
        return null;
    }

    public CartItemResponse getDefaultAddToCart(String userId, CartItemRequestDTO request, Exception e) {
        System.out.println("Retry fallback Called due to: " + e.getMessage());
        return null;
    }

    @Override
    @Transactional
    public String deleteFromCart(String userId, Long productId) {
        UserResponseDTO userResponse=userServiceClient.getUserById(userId);
        if (userResponse == null ||
                userResponse.getId() == null ||
                userResponse.getName() == null ||
                userResponse.getEmail() == null) {
            throw new APIException("Invalid user response received from UserService");
        }
        ProductResponseDTO productResponseDTO=productServiceClient.getProductById(productId);
        if(productResponseDTO==null){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
//        Optional<User> user=userRepository.findById(Long.parseLong(userId));
//        if(user.isEmpty()){
//            throw new APIException("User not found");
//        }
//        User existingUser=user.get();
//        Optional<Products> product=productRepository.findById(productId);
//        if(product.isEmpty()){
//            throw new APIException("Product not found");
//        }
//        Products existingProduct=product.get();
        cartItemRepository.deleteByUserIdAndProductId(userId,productId);
        return "CartItem Deleted Successfully";

    }

    @Override
    public List<CartItemResponse> getCartItem(String userId) {
        UserResponseDTO userResponse=userServiceClient.getUserById(userId);
        if (userResponse == null ||
                userResponse.getId() == null ||
                userResponse.getName() == null ||
                userResponse.getEmail() == null) {
            throw new APIException("Invalid user response received from UserService");
        }
        List<CartItemResponse>cartItemResponses=cartItemRepository.findByUserId(userId).stream()
                .map(cartItem->
                     modelMapper.map(cartItem,CartItemResponse.class)
                ).toList();
        return cartItemResponses;
    }
}
