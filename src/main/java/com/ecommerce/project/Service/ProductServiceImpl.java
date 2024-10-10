package com.ecommerce.project.Service;

import com.ecommerce.project.Exceptions.APIException;
import com.ecommerce.project.Exceptions.ResourceNotFoundException;
import com.ecommerce.project.Model.Category;
import com.ecommerce.project.Model.Product;
import com.ecommerce.project.Payload.ProductRequestDTO;
import com.ecommerce.project.Payload.ProductResponseDTO;
import com.ecommerce.project.Repository.CategoryRepository;
import com.ecommerce.project.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public Product addProduct(Long categoryId, ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId" , categoryId));
        List<Product> products = category.getProducts();
        boolean isProductNotPresent = true;
        for(Product value : products) {
            if (value.getProductName().equals(productRequestDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if(!isProductNotPresent)
            throw new APIException("Product is already present");
        Product product = modelMapper.map(productRequestDTO, Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() * (1 - (product.getDiscount() * 0.01));
        product.setSpecialPrice(specialPrice);
        return productRepository.save(product);
    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Product> products = productRepository.findAll();
        if(products.isEmpty())
            throw new APIException("No product is present");
        List<ProductRequestDTO> productRequestDTOS = products.stream().map(e -> modelMapper.map(e, ProductRequestDTO.class)).toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        productResponseDTO.setPageNumber(pageNumber);
        productResponseDTO.setPageSize(pageSize);
        productResponseDTO.setTotalElements(categoryPage.getTotalElements());
        productResponseDTO.setTotalPages(categoryPage.getTotalPages());
        productResponseDTO.setLastPage(categoryPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId" , categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        if(products.isEmpty())
            throw new APIException("No product is present");
        List<ProductRequestDTO> productRequestDTOS = products.stream().map(e -> modelMapper.map(e, ProductRequestDTO.class)).toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductsByKeyword(String keyword) {
        //prduct size is 0 or not
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

    @Override
    public ProductResponseDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from DB
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        // Upload image to server
        // Get the file name of uploaded image
        String filename = fileService.uploadImage(path, image);
        // Updating the file name to the product
        productFromDb.setImage(filename);
        // return DTO after mapping product to DTO
        ProductRequestDTO productRequestDTO = modelMapper.map(productFromDb, ProductRequestDTO.class);
        List<ProductRequestDTO> productRequestDTOS = new ArrayList<>();
        productRequestDTOS.add(productRequestDTO);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);
        return productResponseDTO;
    }
}