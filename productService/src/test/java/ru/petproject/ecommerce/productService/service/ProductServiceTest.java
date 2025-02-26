package ru.petproject.ecommerce.productService.service;

import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petproject.ecommerce.productService.dto.ProductDto;
import ru.petproject.ecommerce.productService.exceptions.ProductNotFoundException;
import ru.petproject.ecommerce.productService.exceptions.UserNotAuthException;
import ru.petproject.ecommerce.productService.model.Product;
import ru.petproject.ecommerce.productService.repository.ProductRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import io.jsonwebtoken.Jwts;
import ru.petproject.ecommerce.productService.utils.JwtUtil;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ProductService productService;

    private String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, "your_jwt_secret")
                .compact();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllProducts() {
        Product product = new Product();
        when(productRepository.findByDeletedFalse()).thenReturn(List.of(product));

        List<ProductDto> products = productService.findAllProducts();

        assertEquals(1, products.size());
        verify(productRepository, times(1)).findByDeletedFalse();
    }

    @Test
    void findByIdProduct() {
        Product product = new Product();
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));

        Optional<ProductDto> productDto = productService.findByIdProduct(1L);

        assertTrue(productDto.isPresent());
        verify(productRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void createProductUserAdmin() {
        ProductDto productDto = new ProductDto();
        Product product = new Product();
        String token = generateToken("admin");

        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("admin");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto createdProduct = productService.createProduct(productDto, token);

        assertNotNull(createdProduct);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProductNotAuthorized() {
        ProductDto productDto = new ProductDto();
        String token = "invalid_token";

        when(jwtUtil.isTokenValid(token)).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> {
            productService.createProduct(productDto, token);
        });

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct() {
        ProductDto productDto = new ProductDto();
        Product product = new Product();
        String token = generateToken("admin");

        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("admin");
        when(productRepository.findByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto updatedProduct = productService.updateProduct(1L, productDto, token);

        assertNotNull(updatedProduct);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProductNotFound() {
        ProductDto productDto = new ProductDto();
        String token = generateToken("admin");

        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("admin");
        when(productRepository.findByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(1L, productDto, token);
        });

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct() {
        Product product = new Product();
        String token = generateToken("admin");

        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("admin");
        when(productRepository.findByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(product));

        productService.deleteProduct(1L, token);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProductNotFound() {
        String token = generateToken("admin");

        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("admin");
        when(productRepository.findByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1L, token);
        });

        verify(productRepository, never()).deleteById(any(Long.class));
    }
}
