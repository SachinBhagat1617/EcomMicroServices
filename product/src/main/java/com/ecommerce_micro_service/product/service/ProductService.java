package com.ecommerce_micro_service.product.service;

import com.ecommerce_micro_service.product.dto.ProductRequestDTO;
import com.ecommerce_micro_service.product.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    String deleteProduct(Long id);

    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> searchByKeyword(String keyword);

    ProductResponseDTO getProductsById(Long id);
}
