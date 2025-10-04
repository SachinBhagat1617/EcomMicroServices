package com.ecommerce_micro_service.order.repositories;


import com.ecommerce_micro_service.order.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository <Orders,Long> {
}
