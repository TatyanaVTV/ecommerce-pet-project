package ru.petproject.ecommerce.productService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petproject.ecommerce.productService.dto.CategoryDto;
import ru.petproject.ecommerce.productService.exceptions.CategoryNotFoundException;
import ru.petproject.ecommerce.productService.model.Category;
import ru.petproject.ecommerce.productService.repository.CategoryRepository;

import java.util.Arrays;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllCategories() {
        Category category = new Category(1L, "Категория1", false);
        when(categoryRepository.findByDeletedFalse()).thenReturn(Arrays.asList(category));

        List<CategoryDto> categories = categoryService.findAllCategories();

        assertEquals(1, categories.size());
        assertEquals("Категория1", categories.get(0).getName());
    }

    @Test
    void findByIdCategory() {
        Category category = new Category(1L, "Категория1", false);
        when(categoryRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(category));

        Optional<CategoryDto> categoryDTO = categoryService.findCategoryById(1L);

        assertTrue(categoryDTO.isPresent());
        assertEquals("Категория1", categoryDTO.get().getName());
    }

    @Test
    void createCategory() {
        Category category = new Category(1L, "Категория1", false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto categoryDTO = new CategoryDto();
        categoryDTO.setName("Категория1");

        CategoryDto createdCategory = categoryService.createCategory(categoryDTO);

        assertEquals("Категория1", createdCategory.getName());
    }

    @Test
    void deleteCategory() {
        Category category = new Category(1L, "Категория1", false);
        when(categoryRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).save(any(Category.class));
        assertTrue(category.isDeleted());
    }

    @Test
    void deleteCategory_NotFound() {
        when(categoryRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}

