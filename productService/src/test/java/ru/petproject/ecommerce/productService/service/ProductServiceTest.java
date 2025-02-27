package ru.petproject.ecommerce.productService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
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

@ExtendWith(MockitoExtension.class)
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
       // productService = new ProductService();
        productService.restTemplate = restTemplate;
        productService.userServiceUrl = "http://localhost:8080/user-service/isAdmin";
        ReflectionTestUtils.setField(productService, "userServiceUrl", "http://localhost:8080/user-service/isAdmin");
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
    void isAdmin_ReturnsTrue() {
        String token = "validToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(token, headers);

        when(restTemplate.postForObject(eq("http://localhost:8080/user-service/isAdmin"), any(HttpEntity.class), eq(Boolean.class)))
                .thenReturn(true);

        boolean result = productService.isAdmin(token);
        assertTrue(result);
    }

    @Test
    void isAdmin_ReturnsFalse() {
        String token = "invalidToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(token, headers);

        when(restTemplate.postForObject(eq("http://localhost:8080/user-service/isAdmin"), any(HttpEntity.class), eq(Boolean.class)))
                .thenReturn(false);

        boolean result = productService.isAdmin(token);
        assertFalse(result);
    }

    @Test
    void createProductUserAdmin() {
        ProductDto productDto = new ProductDto();
        Product product = new Product();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(Boolean.class))).thenReturn(true); // Мокируем, что ответ - true
        when(productRepository.save(any(Product.class))).thenReturn(product);

        var result = productService.createProduct(productDto, "validToken");
        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());

        System.out.println("Created product: " + result); // Добавляем вывод отладочной информации
    }

    @Test
    void createProductNotAuthorized() {
        ProductDto productDto = new ProductDto();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.postForObject(anyString(), any(), eq(Boolean.class))).thenReturn(false);

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
        when(restTemplate.postForObject(anyString(), any(), eq(Boolean.class))).thenReturn(true);
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
        when(restTemplate.postForObject(anyString(), any(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productDto, "validToken"));
    }

    @Test
    void updateProduct_NonAdminUser() {
        ProductDto productDto = new ProductDto();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.postForObject(anyString(), any(), eq(Boolean.class))).thenReturn(false);

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
        //productService = new ProductService();
        Product product = new Product();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.postForObject(anyString(), any(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(product));

        productService.deleteProduct(1L, "validToken");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_NonAdminUser() {
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.postForObject(anyString(), any(), eq(Boolean.class))).thenReturn(false);

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
        when(restTemplate.postForObject(anyString(), any(), eq(Boolean.class))).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L, "validToken"));
    }
}
