package com.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entity.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndCreatedBy(
            String name,
            String createdBy
    );

    Optional<Category> findByNameAndCreatedBy(
            String name,
            String createdBy
    );

    Optional<Category> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    List<Category> findByActiveTrue();

    List<Category> findByCreatedByAndActiveTrue(
            String createdBy
    );
}