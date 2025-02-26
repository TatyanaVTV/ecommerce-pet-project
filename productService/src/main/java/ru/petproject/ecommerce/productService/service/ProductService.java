package ru.petproject.ecommerce.productService.service;

import ru.petproject.ecommerce.productService.dto.ProductDto;
import ru.petproject.ecommerce.productService.exceptions.ProductNotFoundException;
import ru.petproject.ecommerce.productService.exceptions.UserNotAuthException;
import ru.petproject.ecommerce.productService.model.Category;
import ru.petproject.ecommerce.productService.model.Product;
import ru.petproject.ecommerce.productService.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petproject.ecommerce.productService.utils.JwtUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private JwtUtil jwtUtil;

    public List<ProductDto> findAllProducts() {
        return productRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> findByIdProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .map(this::convertToDTO);
    }

    public ProductDto createProduct(ProductDto productDTO, String token) {
        String userId = jwtUtil.extractUserId(token);
        if (jwtUtil.isTokenValid(token)) {
        // if (userListener.isUserAuthorized(userLogin) && userListener.isUserAdmin(userLogin)) {
            Product product = convertToEntity(productDTO);
            Product savedProduct = productRepository.save(product);
            return convertToDTO(savedProduct);
        }
        throw new UserNotAuthException(userId);

    }

    public ProductDto updateProduct(Long id, ProductDto productDTO, String token) {
        String userId = jwtUtil.extractUserId(token);
        if (jwtUtil.isTokenValid(token)) {
        // if (userListener.isUserAuthorized(userId) && userListener.isUserAdmin(userLogin)) {
            Product product = productRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setStock(productDTO.getStock());
            product.setCategoryId(new Category(productDTO.getCategoryId(), null, false));
            product.setUpdated_at(LocalDateTime.now());
            product = productRepository.save(product);
            return convertToDTO(product);
        }
        throw new UserNotAuthException(userId);
    }

    public void deleteProduct(Long id, String token) {
        String userId = jwtUtil.extractUserId(token);
        if (jwtUtil.isTokenValid(token)) {
            Product product = productRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
            product.setDeleted(true);
            productRepository.save(product);
        } else {
            throw new UserNotAuthException(userId);
        }
    }

    private ProductDto convertToDTO(Product product) {
        Long categoryId = (product.getCategoryId() != null) ? product.getCategoryId().getId() : null;
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                categoryId,
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
