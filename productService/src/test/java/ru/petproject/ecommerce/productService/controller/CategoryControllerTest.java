package ru.petproject.ecommerce.productService.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.petproject.ecommerce.productService.dto.CategoryDto;
import ru.petproject.ecommerce.productService.service.CategoryService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void getAllCategories() throws Exception {
        CategoryDto categoryDTO = new CategoryDto();
        categoryDTO.setName("Категория1");
        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(categoryDTO));

        mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Категория1"));
    }

    @Test
    void getCategoryById() throws Exception {
        CategoryDto categoryDTO = new CategoryDto();
        categoryDTO.setName("Категория1");
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.of(categoryDTO));

        mockMvc.perform(get("/api/v1/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Категория1"));
    }

    @Test
    void createCategory() throws Exception {
        CategoryDto categoryDTO = new CategoryDto();
        when(categoryService.createCategory(any(CategoryDto.class), anyString())).thenReturn(categoryDTO);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer test_jwt_token")
                        .content("{\"name\":\"Test Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class), eq("test_jwt_token"));
    }

    @Test
    void deleteCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/1")
                        .header("Authorization", "Bearer test_jwt_token"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategory(anyLong(), eq("test_jwt_token"));
    }
}