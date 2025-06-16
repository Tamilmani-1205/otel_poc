package com.productmanagement.controller;

import com.productmanagement.dto.product.ProductRequest;
import com.productmanagement.dto.product.ProductResponse;
import com.productmanagement.dto.product.ProductSearchRequest;
import com.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        log.info("Received request to get all products. Search: {}, Page: {}, Size: {}", 
                search, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<ProductResponse> response = productService.getAllProducts(search, pageable);
            log.info("Successfully retrieved {} products", response.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get products - Error: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        log.info("Received request to get product by id: {}", id);
        try {
            ProductResponse response = productService.getProductById(id);
            log.info("Successfully retrieved product: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get product: {} - Error: {}", id, e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Received request to create product: {}", request.getName());
        try {
            ProductResponse response = productService.createProduct(request);
            log.info("Successfully created product: {} with id: {}", request.getName(), response.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to create product: {} - Error: {}", request.getName(), e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        log.info("Received request to update product: {} with id: {}", request.getName(), id);
        try {
            ProductResponse response = productService.updateProduct(id, request);
            log.info("Successfully updated product: {} with id: {}", request.getName(), id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to update product: {} with id: {} - Error: {}", 
                    request.getName(), id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        log.info("Received request to delete product with id: {}", id);
        try {
            productService.deleteProduct(id);
            log.info("Successfully deleted product with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete product: {} - Error: {}", id, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(@Valid @RequestBody ProductSearchRequest searchRequest) {
        log.info("Received search request for products with criteria: {}", searchRequest);
        try {
            Page<ProductResponse> response = productService.searchProducts(searchRequest);
            log.info("Successfully found {} products matching search criteria", response.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to search products - Error: {}", e.getMessage());
            throw e;
        }
    }
} 