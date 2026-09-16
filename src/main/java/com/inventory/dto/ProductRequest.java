package com.inventory.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Pattern(
        regexp = "^[A-Za-z\\s]+$",
        message = "Product name must contain only letters and spaces. Numbers are not allowed."
    )
    private String name;

    @NotBlank(message = "SKU is required")
    private String sku;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(
        value = "0.0",
        message = "Price cannot be negative"
    )
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(
        value = 0,
        message = "Quantity cannot be negative"
    )
    private Integer quantity;

    @Min(
        value = 0,
        message = "Low stock threshold cannot be negative"
    )
    private Integer lowStockThreshold = 10;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    // ========================================
    // GETTERS
    // ========================================

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    // ========================================
    // SETTERS
    // ========================================

    public void setName(String name) {
        this.name = name;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setLowStockThreshold(
            Integer lowStockThreshold) {
        this.lowStockThreshold =
                lowStockThreshold;
    }

    public void setCategoryId(
            Long categoryId) {
        this.categoryId = categoryId;
    }
}