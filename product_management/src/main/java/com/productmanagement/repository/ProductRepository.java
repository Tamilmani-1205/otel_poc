package com.productmanagement.repository;

import com.productmanagement.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByName(String name);
    
    @Query(value = """
           SELECT p.* FROM products p 
           WHERE (:name IS NULL OR p.name ILIKE :namePattern)
           AND (:description IS NULL OR p.description ILIKE :descriptionPattern)
           AND (:minPrice IS NULL OR p.price >= :minPrice)
           AND (:maxPrice IS NULL OR p.price <= :maxPrice)
           AND (p.created_at >= :createdAfter)
           AND (p.created_at <= :createdBefore)
           """,
           countQuery = """
           SELECT COUNT(*) FROM products p 
           WHERE (:name IS NULL OR p.name ILIKE :namePattern)
           AND (:description IS NULL OR p.description ILIKE :descriptionPattern)
           AND (:minPrice IS NULL OR p.price >= :minPrice)
           AND (:maxPrice IS NULL OR p.price <= :maxPrice)
           AND (p.created_at >= :createdAfter)
           AND (p.created_at <= :createdBefore)
           """,
           nativeQuery = true)
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("description") String description,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("createdAfter") ZonedDateTime createdAfter,
            @Param("createdBefore") ZonedDateTime createdBefore,
            @Param("namePattern") String namePattern,
            @Param("descriptionPattern") String descriptionPattern,
            Pageable pageable);

    default Page<Product> searchProducts(
            String name,
            String description,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            ZonedDateTime createdAfter,
            ZonedDateTime createdBefore,
            Pageable pageable) {
        String namePattern = name != null ? "%" + name + "%" : null;
        String descriptionPattern = description != null ? "%" + description + "%" : null;
        return searchProducts(name, description, minPrice, maxPrice, createdAfter, createdBefore, namePattern, descriptionPattern, pageable);
    }
} 