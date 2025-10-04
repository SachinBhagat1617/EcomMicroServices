package com.ecommerce_micro_service.order.service;

import com.ecommerce_micro_service.order.dto.CartItemRequestDTO;
import com.ecommerce_micro_service.order.dto.CartItemResponse;

import java.util.List;


public interface CartService {
    CartItemResponse addToCart(String userId, CartItemRequestDTO request);

    String deleteFromCart(String userId, Long productId);

    List<CartItemResponse> getCartItem(String userId);
}
