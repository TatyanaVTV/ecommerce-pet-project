package ru.petproject.ecommerce.productService.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.petproject.ecommerce.productService.dto.CategoryDto;
import ru.petproject.ecommerce.productService.model.Category;
import ru.petproject.ecommerce.productService.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDto> findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO);
    }

    public CategoryDto createCategory(CategoryDto categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDto convertToDTO(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    private Category convertToEntity(CategoryDto categoryDTO) {
        return new Category(
                categoryDTO.getId(),
                categoryDTO.getName()
        );
    }
}
