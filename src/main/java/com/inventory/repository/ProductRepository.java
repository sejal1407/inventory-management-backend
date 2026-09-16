package com.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByQuantityLessThanEqual(Integer quantity);

    List<Product> findByCreatedBy(String createdBy);

    List<Product> findByCreatedByAndActiveTrue(String createdBy);

    List<Product> findByActiveTrue();

    Optional<Product> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );
}