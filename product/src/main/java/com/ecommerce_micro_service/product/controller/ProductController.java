package com.ecommerce_micro_service.product.controller;

import com.ecommerce_micro_service.product.repositories.ProductRepository;
import com.ecommerce_micro_service.product.dto.ProductRequestDTO;
import com.ecommerce_micro_service.product.dto.ProductResponseDTO;
import com.ecommerce_micro_service.product.exceptions.APIException;
import com.ecommerce_micro_service.product.models.Products;
import com.ecommerce_micro_service.product.service.CloudinaryService;
import com.ecommerce_micro_service.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    ProductRepository productRepository;

    @PostMapping(path = "/products")  // Remove consumes attribute completely
    public ResponseEntity<ProductResponseDTO> addProducts(
            @RequestPart(value = "product", required = true) String productString,  // Change to String
            @RequestPart(value = "image", required = true) MultipartFile image) {
        try {
            //recommend sticking with the original approach (using String for productRequestDTO)
            // as it's more reliable when dealing with multipart requests

            ObjectMapper mapper = new ObjectMapper();
            ProductRequestDTO productRequestDTO = mapper.readValue(productString, ProductRequestDTO.class);
            //- `ObjectMapper.readValue()`: JSON String → Java Object
            String imageUrl = cloudinaryService.uploadImage(image);
            productRequestDTO.setImageUrl(imageUrl);
            ProductResponseDTO productResponseDTO = productService.addProduct(productRequestDTO);
            return ResponseEntity.ok(productResponseDTO);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/products/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestPart(value="product",required = true) String productString,
            @RequestPart(value = "image",required=true) MultipartFile image
    ){
        try{
            Products existingProduct = productRepository.findById(id)
                    .orElseThrow(()->new APIException("Product with given Id not found"));
            ObjectMapper mapper=new ObjectMapper();
            ProductRequestDTO productRequestDTO=mapper.readValue(productString, ProductRequestDTO.class);
            // If new image is provided, upload it and update the URL
            if(image!=null && !image.isEmpty()){
                String oldImageUrl=existingProduct.getImageUrl();
                if(oldImageUrl!=null && !oldImageUrl.isEmpty()){
                    cloudinaryService.deleteImage(oldImageUrl);
                }
                String newImageUrl=cloudinaryService.uploadImage(image);
                productRequestDTO.setImageUrl(newImageUrl);

            }else{
                // If no new image is provided, keep the old image URL
                productRequestDTO.setImageUrl(existingProduct.getImageUrl());
            }
            ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequestDTO);
            return ResponseEntity.ok(updatedProduct);

        }catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProducts(
        @PathVariable Long id
    ){
        String response=productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> getProductsById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductsById(id));
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductResponseDTO>> searchByKeyword(
            @RequestParam String keyword
    ){
        return ResponseEntity.ok(productService.searchByKeyword(keyword));
    }


}
