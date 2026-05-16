package com.example.productservice.service;
import com.example.productservice.dto.CategoryRequest;
import com.example.productservice.dto.CategoryResponse;
import com.example.productservice.entity.Category;


import com.example.productservice.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setParentId(request.getParentId());
        category.setIsDeleted(false);

        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(String id, CategoryRequest request) {
        Category category = getActiveEntityOrThrow(id);
        category.setName(request.getName());
        category.setParentId(request.getParentId());
        return toResponse(categoryRepository.save(category));
    }

    public void softDelete(String id) {
        Category category = getActiveEntityOrThrow(id);
        category.setIsDeleted(true);

        categoryRepository.save(category);
    }

    public CategoryResponse getById(String id) {
        return toResponse(getActiveEntityOrThrow(id));
    }



    private Category getActiveEntityOrThrow(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (Boolean.TRUE.equals(category.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        return category;
    }

    private String safeSortBy(String sortBy) {
        if ("name".equals(sortBy) || "parentId".equals(sortBy) || "id".equals(sortBy)) {
            return sortBy;
        }
        return "id";
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getParentId());
    }
}

