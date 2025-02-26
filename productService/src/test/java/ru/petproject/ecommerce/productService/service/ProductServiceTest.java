package ru.petproject.ecommerce.productService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import ru.petproject.ecommerce.productService.dto.ProductDto;
import ru.petproject.ecommerce.productService.exceptions.ProductNotFoundException;
import ru.petproject.ecommerce.productService.exceptions.UserNotAuthException;
import ru.petproject.ecommerce.productService.model.Product;
import ru.petproject.ecommerce.productService.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import ru.petproject.ecommerce.productService.utils.JwtUtil;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;

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
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        var result = productService.createProduct(productDto, "validToken");
        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
    }

    @Test
    void createProductNotAuthorized() {
        ProductDto productDto = new ProductDto();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> productService.createProduct(productDto, "validToken"));
    }

    @Test
    void createProduct_InvalidToken() {
        ProductDto productDto = new ProductDto();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> productService.createProduct(productDto, "invalidToken"));
    }

    @Test
    void updateProduct() {
        ProductDto productDto = new ProductDto();
        Product product = new Product();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        var result = productService.updateProduct(1L, productDto, "validToken");
        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
    }

    @Test
    void updateProductNotFound() {
        ProductDto productDto = new ProductDto();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productDto, "validToken"));
    }

    @Test
    void updateProduct_NonAdminUser() {
        ProductDto productDto = new ProductDto();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> productService.updateProduct(1L, productDto, "validToken"));
    }

    @Test
    void updateProduct_InvalidToken() {
        ProductDto productDto = new ProductDto();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> productService.updateProduct(1L, productDto, "invalidToken"));
    }

    @Test
    void deleteProduct() {
        Product product = new Product();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(product));

        productService.deleteProduct(1L, "validToken");
        verify(productRepository).save(any(Product.class));
    }
    @Test
    void deleteProduct_NonAdminUser() {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> productService.deleteProduct(1L, "validToken"));
    }

    @Test
    void deleteProduct_InvalidToken() {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> productService.deleteProduct(1L, "invalidToken"));
    }
        @Test
    void deleteProductNotFound() {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L, "validToken"));
    }
}
