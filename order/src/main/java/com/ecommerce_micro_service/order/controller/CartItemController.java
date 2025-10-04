package com.ecommerce_micro_service.order.controller;

import com.ecommerce_micro_service.order.dto.CartItemRequestDTO;
import com.ecommerce_micro_service.order.dto.CartItemResponse;
import com.ecommerce_micro_service.order.service.CartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Transactional
public class CartItemController {
    private final CartService cartService;
    private int attempt=0;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequestDTO request
    ) {
        CartItemResponse cartIemResponse = cartService.addToCart(userId, request);
        if(cartIemResponse==null){
            return ResponseEntity.badRequest().body("Not able to complete request");
        }
        return ResponseEntity.ok(cartIemResponse.toString());
    }


    @DeleteMapping("/product")
    public ResponseEntity<String> deleteFromCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestParam Long productId
    ){
        String response=cartService.deleteFromCart(userId,productId);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItem(
            @RequestHeader("X-User-ID") String userId
    ){
        List<CartItemResponse>cartItemResponses=cartService.getCartItem(userId);
        return ResponseEntity.ok(cartItemResponses);
    }
}