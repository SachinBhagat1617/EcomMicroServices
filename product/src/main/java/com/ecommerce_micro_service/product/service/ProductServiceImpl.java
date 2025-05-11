package com.ecommerce_micro_service.product.service;

//import com.ecommerce_micro_service.product.repositories.CartItemRepository;
import com.ecommerce_micro_service.product.repositories.ProductRepository;
import com.ecommerce_micro_service.product.dto.ProductRequestDTO;
import com.ecommerce_micro_service.product.dto.ProductResponseDTO;
import com.ecommerce_micro_service.product.exceptions.APIException;
import com.ecommerce_micro_service.product.exceptions.ResourceNotFoundException;
import com.ecommerce_micro_service.product.models.Products;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryService cloudinaryService;


//    @Autowired
//    CartItemRepository cartItemRepository;

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Products product=modelMapper.map(productRequestDTO, Products.class);
        Products existingProduct=productRepository.findByName(product.getName());
        if(existingProduct!=null){
            throw new APIException("Product already exists");
        }
        Products savedProduct=productRepository.save(product);
        return modelMapper.map(savedProduct,ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Products existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new APIException("Product of given  productId not Found"));
        Products product=modelMapper.map(productRequestDTO, Products.class);
        product.setId(id);  // Explicitly set the ID
        //product.setActive(existingProduct.getActive());  // Maintain active status
        product.setCreatedAt(existingProduct.getCreatedAt());  // Maintain creation timestamp
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setCategory(productRequestDTO.getCategory());

        product.setPrice(productRequestDTO.getPrice());
        product.setStockQuantity(productRequestDTO.getStockQuantity());
        Products updatedProduct=productRepository.save(product);
        ProductResponseDTO productResponseDTO=modelMapper.map(updatedProduct, ProductResponseDTO.class);
        return productResponseDTO;
    }
    @Transactional
    @Override
    public String deleteProduct(Long id) {
        Products product=productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product","ProductId",id));
        // First delete all cart items referencing this product
        //cartItemRepository.deleteByProductId(id);
        // Then delete the product
        String imageUrl=product.getImageUrl();
        cloudinaryService.deleteImage(imageUrl);
        productRepository.deleteById(id);
        return "Product deleted Successfully";
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<ProductResponseDTO> productsList=productRepository.findByActiveTrue().stream()
                .map(product->{
                    return modelMapper.map(product,ProductResponseDTO.class);
                }).toList();
        if(productsList.isEmpty()){
            throw new APIException("No products Added");
        }
        return productsList;
    }

    @Override
    public List<ProductResponseDTO> searchByKeyword(String keyword) {
        List<ProductResponseDTO>productResponseDTOList=productRepository.findByKeyword(keyword).stream()
                .map(product->{
                    return modelMapper.map(product,ProductResponseDTO.class);
                }).toList();
        if(productResponseDTOList.isEmpty()){
            throw new APIException("No products found with your search keyword: "+keyword+" ");
        }
        return productResponseDTOList;
    }

    @Override
    public ProductResponseDTO getProductsById(Long id) {
        Optional<Products> product=productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Product", "ProductId", id);
        }
        ProductResponseDTO productResponseDTO=modelMapper.map(product.get(),ProductResponseDTO.class);
        return productResponseDTO;
    }


}
