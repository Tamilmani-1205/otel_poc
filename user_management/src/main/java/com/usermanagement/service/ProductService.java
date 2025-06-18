package com.usermanagement.service;

import com.usermanagement.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final RestTemplate restTemplate;

    @Value("${product.service.url:http://app:8080}")
    private String productServiceUrl;

    public List<ProductResponse> getAllProducts() {
        try {
            String url = productServiceUrl + "/api/products";
            log.info("Calling product service at: {}", url);
            
            ResponseEntity<List<ProductResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>() {}
            );
            
            log.info("Successfully retrieved {} products", response.getBody() != null ? response.getBody().size() : 0);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling product service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve products from product service", e);
        }
    }
} 