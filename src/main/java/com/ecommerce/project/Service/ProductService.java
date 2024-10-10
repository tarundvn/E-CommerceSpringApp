package com.ecommerce.project.Service;

import com.ecommerce.project.Model.Product;
import com.ecommerce.project.Payload.ProductRequestDTO;
import com.ecommerce.project.Payload.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    Product addProduct(Long categoryId, ProductRequestDTO productRequestDTO);

    ProductResponseDTO getAllProducts();

    ProductResponseDTO getProductsByCategory(Long categoryId);

    ProductResponseDTO searchProductsByKeyword(String keyword);

    ProductResponseDTO updateProductByProductId(Product product, Long productId);

    ProductResponseDTO deleteProductByProductId(Long productId);
}