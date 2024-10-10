package com.ecommerce.project.Controller;

import com.ecommerce.project.Config.AppConstants;
import com.ecommerce.project.Model.Product;
import com.ecommerce.project.Payload.ProductRequestDTO;
import com.ecommerce.project.Payload.ProductResponseDTO;
import com.ecommerce.project.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductRequestDTO> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO, @PathVariable Long categoryId) {
        Product product = productService.addProduct(categoryId, productRequestDTO);
        return new ResponseEntity<>(productRequestDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        ProductResponseDTO productResponseDTO = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products/{categoryId}")
    public ResponseEntity<ProductResponseDTO> getProductsByCategory(@PathVariable Long categoryId){
        ProductResponseDTO productResponseDTO = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponseDTO> getProductsByKeyword(@PathVariable String keyword){
        ProductResponseDTO productResponseDTO = productService.searchProductsByKeyword(keyword);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProductByProductId(@Valid @RequestBody Product product, @PathVariable Long productId){
        ProductResponseDTO productResponseDTO = productService.updateProductByProductId(product, productId);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> deleteProductByProductId(@PathVariable Long productId){
        ProductResponseDTO productResponseDTO = productService.deleteProductByProductId(productId);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductResponseDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
        ProductResponseDTO productResponseDTO = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }
}