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
import java.util.Collections;
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
        ProductDto productDTO = new ProductDto();
        productDTO.setName("Товар1");
        when(productService.findAllProducts()).thenReturn(Collections.singletonList(productDTO));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        verify(productService, times(1)).findAllProducts();
    }

    @Test
    void findByIdProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        when(productService.findByIdProduct(1L)).thenReturn(Optional.of(productDto));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(productService, times(1)).findByIdProduct(1L);
    }

    @Test
    void findByIdProductNotFound() throws Exception {
        when(productService.findByIdProduct(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findByIdProduct(1L);
    }

    @Test
    void createProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        when(productService.createProduct(any(ProductDto.class), eq("admin"))).thenReturn(productDto);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userLog", "admin")
                        .content("{ \"name\": \"Test Product\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(productService, times(1)).createProduct(any(ProductDto.class), eq("admin"));
    }

    @Test
    void updateProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        when(productService.updateProduct(eq(1L), any(ProductDto.class), eq("admin"))).thenReturn(productDto);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userLog", "admin")
                        .content("{ \"name\": \"Updated Product\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDto.class), eq("admin"));
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L, "admin");

        mockMvc.perform(delete("/api/v1/products/1")
                        .param("userLog", "admin"))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(1L, "admin");
    }
}
