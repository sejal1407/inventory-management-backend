package com.inventory.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.inventory.dto.CategoryRequest;
import com.inventory.entity.Category;
import com.inventory.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    private static final String ADMIN_EMAIL = "test@gmail.com";

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    /* =========================================================
       GET ALL CATEGORIES
       ADMIN ONLY
       ========================================================= */

    @GetMapping
    public ResponseEntity<?> getAllCategories(
            Authentication authentication) {

        String email = authentication.getName();

        if (!ADMIN_EMAIL.equalsIgnoreCase(email)) {

            return ResponseEntity.status(403)
                    .body(
                            Map.of(
                                    "message",
                                    "Only admin can view all categories."
                            )
                    );
        }

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    /* =========================================================
       GET MY CATEGORIES
       ========================================================= */

    @GetMapping("/my")
    public ResponseEntity<?> getMyCategories(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                categoryService.getMyCategories(email)
        );
    }

    /* =========================================================
       GET AVAILABLE CATEGORIES
       
       Used by Product dropdown.
       ========================================================= */

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCategories(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                categoryService.getAvailableCategories(email)
        );
    }

    /* =========================================================
       CREATE CATEGORY
       ADMIN + USER
       ========================================================= */

    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        try {

            Category category =
                    categoryService.createCategory(
                            request,
                            email
                    );

            return ResponseEntity.ok(category);

        } catch (RuntimeException ex) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /* =========================================================
       UPDATE CATEGORY
       
       ADMIN -> ANY CATEGORY
       USER  -> OWN CATEGORY
       ========================================================= */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        try {

            Category updatedCategory;

            if (ADMIN_EMAIL.equalsIgnoreCase(email)) {

                updatedCategory =
                        categoryService.updateCategoryAsAdmin(
                                id,
                                request,
                                email
                        );

            } else {

                updatedCategory =
                        categoryService.updateOwnCategory(
                                id,
                                request,
                                email
                        );
            }

            return ResponseEntity.ok(
                    updatedCategory
            );

        } catch (RuntimeException ex) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /* =========================================================
       DELETE CATEGORY
       ========================================================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        try {

            if (ADMIN_EMAIL.equalsIgnoreCase(email)) {

                categoryService.deleteCategoryAsAdmin(
                        id,
                        email
                );

            } else {

                categoryService.deleteOwnCategory(
                        id,
                        email
                );
            }

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Category deleted successfully."
                    )
            );

        } catch (RuntimeException ex) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    ex.getMessage()
                            )
                    );
        }
    }
}