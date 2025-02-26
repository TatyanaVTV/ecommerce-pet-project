package ru.petproject.ecommerce.productService.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.petproject.ecommerce.productService.dto.ProductDto;
import ru.petproject.ecommerce.productService.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void findAllProducts() throws Exception {
        List<ProductDto> products = new ArrayList<>();
        when(productService.findAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findAllProducts();
    }

    @Test
    void findByIdProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        when(productService.findByIdProduct(anyLong())).thenReturn(Optional.of(productDto));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(productService, times(1)).findByIdProduct(anyLong());
    }

    @Test
    void createProduct() throws Exception {
        //ProductDto productDto = new ProductDto();
        ProductDto savedProductDto = new ProductDto();
        when(productService.createProduct(any(ProductDto.class), anyString())).thenReturn(savedProductDto);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer test_jwt_token")
                        .content("{\"name\":\"Test Product\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isNotEmpty());

        verify(productService, times(1)).createProduct(any(ProductDto.class), eq("test_jwt_token"));
    }

    @Test
    void updateProduct() throws Exception {
        ProductDto updatedProductDto = new ProductDto();
        when(productService.updateProduct(anyLong(), any(ProductDto.class), anyString())).thenReturn(updatedProductDto);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer test_jwt_token")
                        .content("{\"name\":\"Updated Product\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isNotEmpty());

        verify(productService, times(1)).updateProduct(anyLong(), any(ProductDto.class), eq("test_jwt_token"));
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1")
                        .header("Authorization", "Bearer test_jwt_token"))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(anyLong(), eq("test_jwt_token"));
    }
}
