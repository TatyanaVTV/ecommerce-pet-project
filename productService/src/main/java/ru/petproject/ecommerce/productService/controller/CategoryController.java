package ru.petproject.ecommerce.productService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.petproject.ecommerce.productService.dto.CategoryDto;
import ru.petproject.ecommerce.productService.exceptions.CategoryNotFoundException;
import ru.petproject.ecommerce.productService.service.CategoryService;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findAllCategories() {
        logger.info("Вывод всех категорий");
        return categoryService.findAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable Long id) {
        logger.info("Вывод категории с id: {}", id);
        return categoryService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDTO, @RequestHeader("Authorization") String token) {
        logger.info("Создание категории");
        token = token.replace("Bearer ", "");
        return categoryService.createCategory(categoryDTO, token);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        logger.info("Удаление категории с id: {}", id);
        token = token.replace("Bearer ", "");
        categoryService.deleteCategory(id, token);
    }
}
