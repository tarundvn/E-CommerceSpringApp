package com.ecommerce.project.Controller;

import com.ecommerce.project.Model.Product;
import com.ecommerce.project.Payload.ProductRequestDTO;
import com.ecommerce.project.Payload.ProductResponseDTO;
import com.ecommerce.project.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductRequestDTO> addProduct(@RequestBody ProductRequestDTO productRequestDTO, @PathVariable Long categoryId) {
        Product product = productService.addProduct(categoryId, productRequestDTO);
        return new ResponseEntity<>(productRequestDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProducts() {
        ProductResponseDTO productResponseDTO = productService.getAllProducts();
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
    public ResponseEntity<ProductResponseDTO> updateProductByProductId(@RequestBody Product product, @PathVariable Long productId){
        ProductResponseDTO productResponseDTO = productService.updateProductByProductId(product, productId);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> deleteProductByProductId(@PathVariable Long productId){
        ProductResponseDTO productResponseDTO = productService.deleteProductByProductId(productId);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }
}