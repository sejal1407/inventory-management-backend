package com.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inventory.dto.CategoryRequest;
import com.inventory.entity.Category;
import com.inventory.repository.CategoryRepository;

@Service
public class CategoryService {

    private static final String ADMIN_EMAIL = "test@gmail.com";
    private static final String SYSTEM_USER = "SYSTEM";

    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    /* =========================================================
       CREATE CATEGORY
       ========================================================= */

    public Category createCategory(
            CategoryRequest request,
            String loggedInEmail) {

        String name = request.getName().trim();

        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException(
                    "Category already exists."
            );
        }

        Category category = new Category();

        category.setName(name);
        category.setActive(true);

        category.setCreatedBy(loggedInEmail);
        category.setUpdatedBy(loggedInEmail);

        return categoryRepository.save(category);
    }

    /* =========================================================
       GET ALL ACTIVE CATEGORIES
       ADMIN
       ========================================================= */

    public List<Category> getAllCategories() {
        return categoryRepository.findByActiveTrue();
    }

    /* =========================================================
       GET MY CATEGORIES
       ========================================================= */

    public List<Category> getMyCategories(
            String loggedInEmail) {

        return categoryRepository
                .findByCreatedByAndActiveTrue(
                        loggedInEmail
                );
    }

    /* =========================================================
       AVAILABLE CATEGORIES
       
       SYSTEM + ADMIN + CURRENT USER
       ========================================================= */

    public List<Category> getAvailableCategories(
            String loggedInEmail) {

        return categoryRepository
                .findByActiveTrue()
                .stream()
                .filter(category -> {

                    String owner = category.getCreatedBy();

                    return SYSTEM_USER.equalsIgnoreCase(owner)
                            || ADMIN_EMAIL.equalsIgnoreCase(owner)
                            || loggedInEmail.equalsIgnoreCase(owner);
                })
                .collect(Collectors.toList());
    }

    /* =========================================================
       UPDATE CATEGORY - ADMIN
       
       Admin can edit ANY category.
       ========================================================= */

    public Category updateCategoryAsAdmin(
            Long id,
            CategoryRequest request,
            String loggedInEmail) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Category not found."
                        )
                );

        String newName = request.getName().trim();

        Category existing =
                categoryRepository
                        .findByName(newName)
                        .orElse(null);

        if (existing != null
                && !existing.getId().equals(id)) {

            throw new RuntimeException(
                    "Category already exists."
            );
        }

        category.setName(newName);
        category.setUpdatedBy(loggedInEmail);

        return categoryRepository.save(category);
    }

    /* =========================================================
       UPDATE OWN CATEGORY - USER
       ========================================================= */

    public Category updateOwnCategory(
            Long id,
            CategoryRequest request,
            String loggedInEmail) {

        Category category =
                categoryRepository
                        .findByIdAndCreatedBy(
                                id,
                                loggedInEmail
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "You can only edit your own categories."
                                )
                        );

        String newName = request.getName().trim();

        Category existing =
                categoryRepository
                        .findByName(newName)
                        .orElse(null);

        if (existing != null
                && !existing.getId().equals(id)) {

            throw new RuntimeException(
                    "Category already exists."
            );
        }

        category.setName(newName);
        category.setUpdatedBy(loggedInEmail);

        return categoryRepository.save(category);
    }

    /* =========================================================
       DELETE CATEGORY - ADMIN
       ========================================================= */

    public void deleteCategoryAsAdmin(
            Long id,
            String loggedInEmail) {

        Category category =
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Category not found."
                                )
                        );

        categoryRepository.delete(category);
    }

    /* =========================================================
       DELETE OWN CATEGORY - USER
       ========================================================= */

    public void deleteOwnCategory(
            Long id,
            String loggedInEmail) {

        Category category =
                categoryRepository
                        .findByIdAndCreatedBy(
                                id,
                                loggedInEmail
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "You can only delete your own categories."
                                )
                        );

        categoryRepository.delete(category);
    }
}