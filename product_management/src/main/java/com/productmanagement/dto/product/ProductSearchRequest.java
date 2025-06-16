package com.productmanagement.dto.product;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class ProductSearchRequest {
    private String name;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime createdAfter;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime createdBefore;
    
    @Min(value = 0, message = "Page number must be greater than or equal to 0")
    private Integer page = 0;
    
    @Min(value = 1, message = "Page size must be greater than 0")
    private Integer size = 10;
    
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
} 