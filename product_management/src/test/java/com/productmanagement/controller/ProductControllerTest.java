package com.productmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productmanagement.dto.product.ProductSearchRequest;
import com.productmanagement.dto.product.ProductResponse;
import com.productmanagement.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void searchProducts_WithValidRequest_ReturnsOk() throws Exception {
        // Arrange
        ProductSearchRequest searchRequest = new ProductSearchRequest();
        searchRequest.setName("Test");
        searchRequest.setMinPrice(new BigDecimal("10.00"));
        searchRequest.setMaxPrice(new BigDecimal("100.00"));

        ProductResponse response = new ProductResponse();
        response.setName("Test Product");
        response.setPrice(new BigDecimal("50.00"));

        Page<ProductResponse> responsePage = new PageImpl<>(List.of(response));
        when(productService.searchProducts(any(ProductSearchRequest.class))).thenReturn(responsePage);

        // Act & Assert
        mockMvc.perform(post("/api/products/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Product"))
                .andExpect(jsonPath("$.content[0].price").value(50.00));
    }

    @Test
    void searchProducts_WithInvalidRequest_ReturnsBadRequest() throws Exception {
        // Arrange
        ProductSearchRequest searchRequest = new ProductSearchRequest();
        searchRequest.setPage(-1); // Invalid page number

        // Act & Assert
        mockMvc.perform(post("/api/products/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isBadRequest());
    }
} 