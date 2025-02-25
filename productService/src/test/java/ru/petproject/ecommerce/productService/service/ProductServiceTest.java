package ru.petproject.ecommerce.productService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petproject.ecommerce.productService.dto.ProductDto;
import ru.petproject.ecommerce.productService.exceptions.ProductNotFoundException;
import ru.petproject.ecommerce.productService.exceptions.UserNotAuthException;
import ru.petproject.ecommerce.productService.kafka.UserListener;
import ru.petproject.ecommerce.productService.model.Product;
import ru.petproject.ecommerce.productService.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserListener userListener;

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
//
    @Test
    void createProductUserAdmin() {
        ProductDto productDto = new ProductDto();
        Product product = new Product();
        when(userListener.isUserAuthorized("admin")).thenReturn(true);
        when(userListener.isUserAdmin("admin")).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto createdProduct = productService.createProduct(productDto, "admin");

        assertNotNull(createdProduct);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProductNotAuthorized() {
        ProductDto productDto = new ProductDto();
        when(userListener.isUserAuthorized("user")).thenReturn(false);

        assertThrows(UserNotAuthException.class, () -> productService.createProduct(productDto, "user"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct() {
        ProductDto productDto = new ProductDto();
        Product product = new Product();
        when(userListener.isUserAuthorized("admin")).thenReturn(true);
        when(userListener.isUserAdmin("admin")).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto updatedProduct = productService.updateProduct(1L, productDto, "admin");

        assertNotNull(updatedProduct);
        verify(productRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProductNotFound() {
        ProductDto productDto = new ProductDto();
        when(userListener.isUserAuthorized("admin")).thenReturn(true);
        when(userListener.isUserAdmin("admin")).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productDto, "admin"));
        verify(productRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct() {
        Product product = new Product();
        when(userListener.isUserAuthorized("admin")).thenReturn(true);
        when(userListener.isUserAdmin("admin")).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L, "admin");

        verify(productRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProductNotFound() {
        when(userListener.isUserAuthorized("admin")).thenReturn(true);
        when(userListener.isUserAdmin("admin")).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L, "admin"));
        verify(productRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
}
