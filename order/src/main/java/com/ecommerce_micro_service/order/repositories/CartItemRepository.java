package com.ecommerce_micro_service.order.repositories;

import com.ecommerce_micro_service.order.models.CartItem;
//import com.ecommerce_micro_service.order.models.Products;
//import com.ecommerce_micro_service.order.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByProductIdAndUserId(Long productId, String userId);

    void deleteByUserIdAndProductId(String existingUserId, Long existingProductId);

    void deleteByProductId(Long id);

    List<CartItem> findByUserId(String userId);

}
