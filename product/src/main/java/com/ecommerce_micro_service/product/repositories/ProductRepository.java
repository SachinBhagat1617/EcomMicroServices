package com.ecommerce_micro_service.product.repositories;

import com.ecommerce_micro_service.product.dto.ProductResponseDTO;
import com.ecommerce_micro_service.product.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {
    Products findByName(String name);

    List<Products> findByActiveTrue();
    // %iphone%
    @Query("Select p from products as p where p.active=true and p.stockQuantity>0 and lower(p.name) like lower(concat('%',:keyword,'%'))")
    List<Products> findByKeyword(@Param("keyword") String keyword);


    Products findByIdAndActiveTrue(Long id);

    Optional<Products> findById(Long id);
}
