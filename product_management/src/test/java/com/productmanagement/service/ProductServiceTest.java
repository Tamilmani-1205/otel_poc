package com.productmanagement.service;

import com.productmanagement.dto.product.ProductSearchRequest;
import com.productmanagement.dto.product.ProductResponse;
import com.productmanagement.model.Product;
import com.productmanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductSearchRequest searchRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setCreatedAt(ZonedDateTime.now());
        testProduct.setUpdatedAt(ZonedDateTime.now());

        searchRequest = new ProductSearchRequest();
        searchRequest.setName("Test");
        searchRequest.setPage(0);
        searchRequest.setSize(10);
        searchRequest.setSortBy("createdAt");
        searchRequest.setSortDirection("DESC");
    }

    @Test
    void searchProducts_WithValidCriteria_ReturnsMatchingProducts() {
        // Arrange
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));
        when(productRepository.searchProducts(
            any(), any(), any(), any(), any(), any(), any(PageRequest.class)
        )).thenReturn(productPage);

        // Act
        Page<ProductResponse> result = productService.searchProducts(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getName());
    }

    @Test
    void searchProducts_WithPriceRange_ReturnsMatchingProducts() {
        // Arrange
        searchRequest.setMinPrice(new BigDecimal("50.00"));
        searchRequest.setMaxPrice(new BigDecimal("100.00"));
        
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));
        when(productRepository.searchProducts(
            any(), any(), any(), any(), any(), any(), any(PageRequest.class)
        )).thenReturn(productPage);

        // Act
        Page<ProductResponse> result = productService.searchProducts(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(new BigDecimal("99.99"), result.getContent().get(0).getPrice());
    }

    @Test
    void searchProducts_WithDateRange_ReturnsMatchingProducts() {
        // Arrange
        ZonedDateTime now = ZonedDateTime.now();
        searchRequest.setCreatedAfter(now.minusDays(1));
        searchRequest.setCreatedBefore(now.plusDays(1));
        
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));
        when(productRepository.searchProducts(
            any(), any(), any(), any(), any(), any(), any(PageRequest.class)
        )).thenReturn(productPage);

        // Act
        Page<ProductResponse> result = productService.searchProducts(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertNotNull(result.getContent().get(0).getCreatedAt());
    }

    @Test
    void searchProducts_WithEmptyResults_ReturnsEmptyPage() {
        // Arrange
        Page<Product> emptyPage = new PageImpl<>(List.of());
        when(productRepository.searchProducts(
            any(), any(), any(), any(), any(), any(), any(PageRequest.class)
        )).thenReturn(emptyPage);

        // Act
        Page<ProductResponse> result = productService.searchProducts(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }
} 