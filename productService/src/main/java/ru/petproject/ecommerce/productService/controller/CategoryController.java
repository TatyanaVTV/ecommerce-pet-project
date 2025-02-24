package ru.petproject.ecommerce.productService.controller;

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

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findAllCategories() {
        return categoryService.findAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
