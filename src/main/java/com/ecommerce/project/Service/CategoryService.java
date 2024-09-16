package com.ecommerce.project.Service;
import com.ecommerce.project.Payload.CategoryRequestDTO;
import com.ecommerce.project.Payload.CategoryResponseDTO;

public interface CategoryService {
    CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO);

    CategoryRequestDTO deleteCategory(Long categoryId);

    CategoryRequestDTO updateCategory(CategoryRequestDTO category, Long categoryId);
}