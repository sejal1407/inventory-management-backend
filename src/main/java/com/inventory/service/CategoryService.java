package com.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.dto.CategoryRequest;
import com.inventory.entity.Category;
import com.inventory.repository.CategoryRepository;

import com.inventory.exception.DuplicateResourceException;
import com.inventory.exception.ResourceNotFoundException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryRequest request) {

        if (categoryRepository.existsByName(request.getName())) {
        	throw new DuplicateResourceException("Category already exists");
        }

        Category category = new Category();

        category.setName(request.getName());
       
        category.setActive(true);
        category.setCreatedBy("SYSTEM");
        category.setUpdatedBy("SYSTEM");

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));
    }

    public Category updateCategory(Long id, CategoryRequest request) {

        Category category = getCategoryById(id);

        category.setName(request.getName());
   
        category.setUpdatedBy("SYSTEM");

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {

        Category category = getCategoryById(id);

        category.setActive(false);
        category.setUpdatedBy("SYSTEM");

        categoryRepository.save(category);
    }
}
