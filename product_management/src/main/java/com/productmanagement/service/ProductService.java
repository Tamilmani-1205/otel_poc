package com.productmanagement.service;

import com.productmanagement.dto.product.ProductRequest;
import com.productmanagement.dto.product.ProductResponse;
import com.productmanagement.dto.product.ProductSearchRequest;
import com.productmanagement.exception.BusinessException;
import com.productmanagement.exception.ResourceNotFoundException;
import com.productmanagement.model.Product;
import com.productmanagement.model.User;
import com.productmanagement.repository.ProductRepository;
import com.productmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Page<ProductResponse> getAllProducts(String search, Pageable pageable) {
        Page<Product> products = search != null && !search.isEmpty()
                ? productRepository.findByNameContainingIgnoreCase(search, pageable)
                : productRepository.findAll(pageable);

        return products.map(this::mapToResponse);
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new BusinessException("Product with this name already exists");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCreatedBy(currentUser);
        product.setUpdatedBy(currentUser);

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (!product.getName().equals(request.getName()) && 
            productRepository.existsByName(request.getName())) {
            throw new BusinessException("Product with this name already exists");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setUpdatedBy(currentUser);

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    public Page<ProductResponse> searchProducts(ProductSearchRequest searchRequest) {
        log.debug("Searching products with criteria: {}", searchRequest);
        
        Sort.Direction direction = Sort.Direction.fromString(searchRequest.getSortDirection());
        Pageable pageable = PageRequest.of(
            searchRequest.getPage(),
            searchRequest.getSize(),
            Sort.by(direction, searchRequest.getSortBy())
        );

        try {
            Page<Product> products = productRepository.searchProducts(
                searchRequest.getName(),
                searchRequest.getDescription(),
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice(),
                searchRequest.getCreatedAfter(),
                searchRequest.getCreatedBefore(),
                pageable
            );

            log.debug("Found {} products matching search criteria", products.getTotalElements());
            return products.map(this::mapToResponse);
        } catch (Exception e) {
            log.error("Error occurred while searching products", e);
            throw new BusinessException("Error occurred while searching products: " + e.getMessage());
        }
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        
        if (product.getCreatedBy() != null) {
            response.setCreatedBy(product.getCreatedBy().getUsername());
        }
        if (product.getUpdatedBy() != null) {
            response.setUpdatedBy(product.getUpdatedBy().getUsername());
        }
        
        return response;
    }
} 