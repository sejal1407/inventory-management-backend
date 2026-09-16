package com.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Pattern(
        regexp = "^[A-Za-z\\s]+$",
        message = "Category name must contain only letters and spaces. Numbers are not allowed."
    )
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}