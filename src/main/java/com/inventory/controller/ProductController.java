package com.inventory.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.inventory.dto.ProductRequest;
import com.inventory.entity.Product;
import com.inventory.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private static final String ADMIN_EMAIL = "test@gmail.com";

    private final ProductService productService;

    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }

    /* =========================================================
       GET ALL PRODUCTS
       ADMIN ONLY
       ========================================================= */

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            Authentication authentication) {

        String email = authentication.getName();

        if (!ADMIN_EMAIL.equalsIgnoreCase(email)) {

            return ResponseEntity.status(403)
                    .body(
                            Map.of(
                                    "message",
                                    "Only admin can view all products."
                            )
                    );
        }

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    /* =========================================================
       GET MY PRODUCTS
       ========================================================= */

    @GetMapping("/my")
    public ResponseEntity<?> getMyProducts(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                productService.getMyProducts(email)
        );
    }

    /* =========================================================
       GET PRODUCT BY ID
       ========================================================= */

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    productService.getProductById(id)
            );

        } catch (RuntimeException ex) {

            return ResponseEntity.status(404)
                    .body(
                            Map.of(
                                    "message",
                                    ex.getMessage()
                            )
                    );
        }
    }

    /* =========================================================
       CREATE PRODUCT
       ADMIN + USER
       ========================================================= */

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        try {

            Product product =
                    productService.createProduct(
                            request,
                            email
                    );

            return ResponseEntity.ok(product);

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
       UPDATE PRODUCT
       
       ADMIN -> ANY PRODUCT
       USER  -> OWN PRODUCT
       ========================================================= */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        try {

            Product updatedProduct;

            if (ADMIN_EMAIL.equalsIgnoreCase(email)) {

                updatedProduct =
                        productService.updateProductAsAdmin(
                                id,
                                request,
                                email
                        );

            } else {

                updatedProduct =
                        productService.updateOwnProduct(
                                id,
                                request,
                                email
                        );
            }

            return ResponseEntity.ok(
                    updatedProduct
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
       DELETE PRODUCT
       
       ADMIN -> ANY PRODUCT
       USER  -> OWN PRODUCT
       ========================================================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        try {

            if (ADMIN_EMAIL.equalsIgnoreCase(email)) {

                productService.deleteProductAsAdmin(id);

            } else {

                productService.deleteOwnProduct(
                        id,
                        email
                );
            }

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Product deleted successfully."
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