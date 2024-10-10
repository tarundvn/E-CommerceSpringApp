package com.ecommerce.project.Controller;
import com.ecommerce.project.Config.AppConstants;
import com.ecommerce.project.Payload.CategoryRequestDTO;
import com.ecommerce.project.Payload.CategoryResponseDTO;
import com.ecommerce.project.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api") // Definition at the class level
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("public/category")
    public ResponseEntity<CategoryResponseDTO> getCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageSize,
                                                            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder), HttpStatusCode.valueOf(200));
    }

    @PostMapping("public/category")
    public ResponseEntity<CategoryRequestDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return new ResponseEntity<>(categoryService.createCategory(categoryRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<CategoryRequestDTO> deleteCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }

    @PutMapping("public/categories/{categoryId}")
    public ResponseEntity<CategoryRequestDTO> updateCategory(@RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryRequestDTO, categoryId), HttpStatus.OK);
    }
}