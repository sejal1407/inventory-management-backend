package com.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.dto.ProductRequest;
import com.inventory.entity.Category;
import com.inventory.entity.Product;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;

import com.inventory.exception.DuplicateResourceException;
import com.inventory.exception.ResourceNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product createProduct(ProductRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("SKU already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();

        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setLowStockThreshold(request.getLowStockThreshold());
        product.setCategory(category);
        product.setActive(true);
        product.setCreatedBy("SYSTEM");
        product.setUpdatedBy("SYSTEM");

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product updateProduct(Long id, ProductRequest request) {

        Product product = getProductById(id);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setLowStockThreshold(request.getLowStockThreshold());
        product.setCategory(category);
        product.setUpdatedBy("SYSTEM");

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {

        Product product = getProductById(id);

        product.setActive(false);
        product.setUpdatedBy("SYSTEM");

        productRepository.save(product);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getLowStockProducts(Integer quantity) {
        return productRepository.findByQuantityLessThanEqual(quantity);
    }

    public Product updateStockQuantity(Long productId, Integer quantity) {

        Product product = getProductById(productId);

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        product.setQuantity(quantity);
        product.setUpdatedBy("SYSTEM");

        return productRepository.save(product);
    }
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}