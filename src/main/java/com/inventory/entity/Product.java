package com.inventory.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    // ========================================
    // ID
    // ========================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========================================
    // PRODUCT NAME
    // ========================================
    @Column(nullable = false)
    private String name;

    // ========================================
    // SKU
    // ========================================
    @Column(nullable = false, unique = true)
    private String sku;

    // ========================================
    // DESCRIPTION
    // ========================================
    private String description;

    // ========================================
    // PRICE
    // ========================================
    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal price;

    // ========================================
    // QUANTITY
    // ========================================
    @Column(nullable = false)
    private Integer quantity;

    // ========================================
    // LOW STOCK THRESHOLD
    // ========================================
    @Column(nullable = false)
    private Integer lowStockThreshold = 10;

    // ========================================
    // CATEGORY
    // ========================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "category_id",
        nullable = false
    )
    private Category category;

    // ========================================
    // STATUS
    // ========================================
    @Column(nullable = false)
    private Boolean active = true;

    // ========================================
    // CREATED BY
    // ========================================
    private String createdBy;

    // ========================================
    // UPDATED BY
    // ========================================
    private String updatedBy;

    // ========================================
    // CREATED AT
    // ========================================
    private LocalDateTime createdAt;

    // ========================================
    // UPDATED AT
    // ========================================
    private LocalDateTime updatedAt;

    // ========================================
    // PRE PERSIST
    // ========================================
    @PrePersist
    public void prePersist() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    // ========================================
    // PRE UPDATE
    // ========================================
    @PreUpdate
    public void preUpdate() {

        updatedAt =
                LocalDateTime.now();
    }

    // ========================================
    // GETTERS
    // ========================================

    public Long getId() {
        return id;
    }

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

    public Category getCategory() {
        return category;
    }

    public Boolean getActive() {
        return active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ========================================
    // SETTERS
    // ========================================

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt) {

        this.updatedAt = updatedAt;
    }
}