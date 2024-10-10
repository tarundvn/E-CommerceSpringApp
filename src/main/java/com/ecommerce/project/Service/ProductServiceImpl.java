package com.ecommerce.project.Service;

import com.ecommerce.project.Exceptions.ResourceNotFoundException;
import com.ecommerce.project.Model.Category;
import com.ecommerce.project.Model.Product;
import com.ecommerce.project.Payload.ProductRequestDTO;
import com.ecommerce.project.Payload.ProductResponseDTO;
import com.ecommerce.project.Repository.CategoryRepository;
import com.ecommerce.project.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Product addProduct(Long categoryId, ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId" , categoryId));
        Product product = modelMapper.map(productRequestDTO, Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() * (1 - (product.getDiscount() * 0.01));
        product.setSpecialPrice(specialPrice);
        return productRepository.save(product);
    }

    @Override
    public ProductResponseDTO getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductRequestDTO> productRequestDTOS = products.stream().map(e -> modelMapper.map(e, ProductRequestDTO.class)).toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId" , categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductRequestDTO> productRequestDTOS = products.stream().map(e -> modelMapper.map(e, ProductRequestDTO.class)).toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductRequestDTO> productRequestDTOS = products.stream().map(e -> modelMapper.map(e, ProductRequestDTO.class)).toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO updateProductByProductId(Product product, Long productId) {
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId" , productId));
        productFromDb.setProductId(product.getProductId());
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setSpecialPrice(product.getSpecialPrice());
        Product savedProduct = productRepository.save(productFromDb);
        ProductRequestDTO productRequestDTO = modelMapper.map(savedProduct, ProductRequestDTO.class);
        List<ProductRequestDTO> productRequestDTOS = new ArrayList<>();
        productRequestDTOS.add(productRequestDTO);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO deleteProductByProductId(Long productId) {
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId" , productId));
        productRepository.delete(productFromDb);
        ProductRequestDTO productRequestDTO = modelMapper.map(productFromDb, ProductRequestDTO.class);
        List<ProductRequestDTO> productRequestDTOS = new ArrayList<>();
        productRequestDTOS.add(productRequestDTO);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        return productResponseDTO;
    }
}