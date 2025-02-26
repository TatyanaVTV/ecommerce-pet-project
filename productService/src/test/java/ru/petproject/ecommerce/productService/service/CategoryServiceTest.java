package ru.petproject.ecommerce.productService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import ru.petproject.ecommerce.productService.dto.CategoryDto;
import ru.petproject.ecommerce.productService.model.Category;
import ru.petproject.ecommerce.productService.repository.CategoryRepository;
import ru.petproject.ecommerce.productService.utils.JwtUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllCategories() {
        Category category = new Category();
        when(categoryRepository.findByDeletedFalse()).thenReturn(List.of(category));

        List<CategoryDto> categories = categoryService.findAllCategories();

        assertEquals(1, categories.size());
        verify(categoryRepository, times(1)).findByDeletedFalse();
    }

    @Test
    void findByIdCategory() {
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(category));
        var result = categoryService.findCategoryById(1L);
        assertTrue(result.isPresent());
        assertEquals(categoryDto.getName(), result.get().getName());
   }

    @Test
    void createCategory() {
        CategoryDto categoryDto = new CategoryDto();
        Category category = new Category();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        var result = categoryService.createCategory(categoryDto, "validToken");
        assertNotNull(result);
        assertEquals(categoryDto.getName(), result.getName());
    }

    @Test
    void deleteCategory() {
        Category category = new Category();
        when(jwtUtil.isTokenValid(anyString())).thenReturn(true);
        when(jwtUtil.extractUserId(anyString())).thenReturn("user1");
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(categoryRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L, "validToken");
        verify(categoryRepository).save(any(Category.class));
    }

}

