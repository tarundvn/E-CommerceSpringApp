package com.ecommerce.project.Service;

import com.ecommerce.project.Model.Product;
import com.ecommerce.project.Payload.ProductRequestDTO;
import com.ecommerce.project.Payload.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    Product addProduct(Long categoryId, ProductRequestDTO productRequestDTO);

    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getProductsByCategory(Long categoryId);

    ProductResponseDTO searchProductsByKeyword(String keyword);

    ProductResponseDTO updateProductByProductId(Product product, Long productId);

    ProductResponseDTO deleteProductByProductId(Long productId);

    ProductResponseDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}