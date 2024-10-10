package com.ecommerce.project.Service;

import com.ecommerce.project.Exceptions.APIException;
import com.ecommerce.project.Exceptions.ResourceNotFoundException;
import com.ecommerce.project.Model.Category;
import com.ecommerce.project.Payload.CategoryRequestDTO;
import com.ecommerce.project.Payload.CategoryResponseDTO;
import com.ecommerce.project.Repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

//        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
//        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new APIException("No Category Created till now");
        }
        List<CategoryRequestDTO> categoryResponseDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryRequestDTO.class)).toList();
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(categoryResponseDTOS);
//        categoryResponseDTO.setPageNumber(pageNumber);
//        categoryResponseDTO.setPageSize(pageSize);
//        categoryResponseDTO.setTotalElements(categoryPage.getTotalElements());
//        categoryResponseDTO.setTotalPages(categoryPage.getTotalPages());
//        categoryResponseDTO.setLastPage(categoryPage.isLast());
        return categoryResponseDTO;
    }

    @Override
    public CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Optional<Category> categoryFromDB = categoryRepository.findByCategoryName(categoryRequestDTO.getCategoryName());
        if(categoryFromDB.isPresent())
            throw new APIException("Category with the name " + categoryRequestDTO.getCategoryName());
        Category category = modelMapper.map(categoryRequestDTO, Category.class);
        categoryRepository.save(category);
        return categoryRequestDTO;
    }

    @Override
    public CategoryRequestDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId" , categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId) {
        Category categoryFromDB = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId" , categoryId));
        Category category = modelMapper.map(categoryRequestDTO, Category.class);
        category.setCategoryId(categoryId);
        return modelMapper.map(category, CategoryRequestDTO.class);
    }
}