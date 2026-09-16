package com.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.dto.ProductRequest;
import com.inventory.entity.Category;
import com.inventory.entity.Product;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;

@Service
public class ProductService {

    private static final String ADMIN_EMAIL = "test@gmail.com";
    private static final String SYSTEM_USER = "SYSTEM";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /* =========================================================
       CREATE PRODUCT
       ========================================================= */

    public Product createProduct(
            ProductRequest request,
            String loggedInEmail) {

        String sku = request.getSku().trim();

        if (productRepository.existsBySku(sku)) {
            throw new RuntimeException("SKU already exists.");
        }

        Category category = getAllowedCategory(
                request.getCategoryId(),
                loggedInEmail
        );

        Product product = new Product();

        product.setName(request.getName().trim());
        product.setSku(sku);

        product.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        product.setLowStockThreshold(
                request.getLowStockThreshold() == null
                        ? 10
                        : request.getLowStockThreshold()
        );

        product.setCategory(category);
        product.setActive(true);

        product.setCreatedBy(loggedInEmail);
        product.setUpdatedBy(loggedInEmail);

        return productRepository.save(product);
    }

    /* =========================================================
       GET ALL PRODUCTS
       ADMIN
       ========================================================= */

    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }

    /* =========================================================
       GET MY PRODUCTS
       ========================================================= */

    public List<Product> getMyProducts(
            String loggedInEmail) {

        return productRepository
                .findByCreatedByAndActiveTrue(
                        loggedInEmail
                );
    }

    /* =========================================================
       GET PRODUCT BY ID
       ========================================================= */

    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found."
                        )
                );
    }

    /* =========================================================
       UPDATE PRODUCT - ADMIN
       
       ADMIN CAN EDIT ANY PRODUCT
       ========================================================= */

    public Product updateProductAsAdmin(
            Long id,
            ProductRequest request,
            String loggedInEmail) {

        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found."
                        )
                );

        String newSku = request.getSku().trim();

        /*
         * Make sure another product does not
         * already use this SKU.
         */

        Product existingProduct =
                productRepository.findBySku(newSku)
                        .orElse(null);

        if (existingProduct != null
                && !existingProduct.getId().equals(id)) {

            throw new RuntimeException(
                    "SKU already exists."
            );
        }

        /*
         * Admin can use any active category.
         */

        Category category =
                categoryRepository.findById(
                        request.getCategoryId()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Category not found."
                        )
                );

        if (!Boolean.TRUE.equals(
                category.getActive())) {

            throw new RuntimeException(
                    "Selected category is inactive."
            );
        }

        /*
         * Update all editable fields.
         */

        product.setName(
                request.getName().trim()
        );

        product.setSku(newSku);

        product.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        product.setPrice(request.getPrice());

        product.setQuantity(request.getQuantity());

        product.setLowStockThreshold(
                request.getLowStockThreshold() == null
                        ? 10
                        : request.getLowStockThreshold()
        );

        product.setCategory(category);

        product.setUpdatedBy(loggedInEmail);

        return productRepository.save(product);
    }

    /* =========================================================
       UPDATE OWN PRODUCT - USER
       ========================================================= */

    public Product updateOwnProduct(
            Long id,
            ProductRequest request,
            String loggedInEmail) {

        Product product =
                productRepository
                        .findByIdAndCreatedBy(
                                id,
                                loggedInEmail
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "You can only edit your own products."
                                )
                        );

        String newSku = request.getSku().trim();

        Product existingProduct =
                productRepository.findBySku(newSku)
                        .orElse(null);

        if (existingProduct != null
                && !existingProduct.getId().equals(id)) {

            throw new RuntimeException(
                    "SKU already exists."
            );
        }

        Category category = getAllowedCategory(
                request.getCategoryId(),
                loggedInEmail
        );

        product.setName(
                request.getName().trim()
        );

        product.setSku(newSku);

        product.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        product.setPrice(request.getPrice());

        product.setQuantity(request.getQuantity());

        product.setLowStockThreshold(
                request.getLowStockThreshold() == null
                        ? 10
                        : request.getLowStockThreshold()
        );

        product.setCategory(category);

        product.setUpdatedBy(loggedInEmail);

        return productRepository.save(product);
    }

    /* =========================================================
       DELETE PRODUCT - ADMIN
       ========================================================= */

    public void deleteProductAsAdmin(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product not found."
                                )
                        );

        productRepository.delete(product);
    }

    /* =========================================================
       DELETE OWN PRODUCT - USER
       ========================================================= */

    public void deleteOwnProduct(
            Long id,
            String loggedInEmail) {

        Product product =
                productRepository
                        .findByIdAndCreatedBy(
                                id,
                                loggedInEmail
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "You can only delete your own products."
                                )
                        );

        productRepository.delete(product);
    }

    /* =========================================================
       CHECK CATEGORY PERMISSION
       ========================================================= */

    private Category getAllowedCategory(
            Long categoryId,
            String loggedInEmail) {

        Category category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Category not found."
                                )
                        );

        if (!Boolean.TRUE.equals(
                category.getActive())) {

            throw new RuntimeException(
                    "Selected category is inactive."
            );
        }

        String categoryOwner =
                category.getCreatedBy();

        boolean allowed =
                SYSTEM_USER.equalsIgnoreCase(
                        categoryOwner
                )
                || ADMIN_EMAIL.equalsIgnoreCase(
                        categoryOwner
                )
                || loggedInEmail.equalsIgnoreCase(
                        categoryOwner
                );

        if (!allowed) {
            throw new RuntimeException(
                    "You are not allowed to use this category."
            );
        }

        return category;
    }
}