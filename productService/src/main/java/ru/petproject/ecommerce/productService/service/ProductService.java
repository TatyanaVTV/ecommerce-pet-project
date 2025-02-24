package ru.petproject.ecommerce.productService.service;

import ru.petproject.ecommerce.productService.dto.ProductDto;
import ru.petproject.ecommerce.productService.exceptions.ProductNotFoundException;
import ru.petproject.ecommerce.productService.model.Category;
import ru.petproject.ecommerce.productService.model.Product;
import ru.petproject.ecommerce.productService.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDto> findAllProducts() {
        return productRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> findByIdProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .map(this::convertToDTO);
    }

    public ProductDto createProduct(ProductDto productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setDeleted(true);
        productRepository.save(product);
    }

    private ProductDto convertToDTO(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategoryId().getId(),
                product.getCreated_at(),
                product.getUpdated_at(),
                product.isDeleted()
        );
    }

    private Product convertToEntity(ProductDto productDTO) {
        Category category = new Category();
        category.setId(productDTO.getCategoryId());

        return new Product(
                productDTO.getId(),
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getStock(),
                category,
                productDTO.getCreatedAt(),
                productDTO.getUpdatedAt(),
                productDTO.isDeleted()
        );
    }
}
