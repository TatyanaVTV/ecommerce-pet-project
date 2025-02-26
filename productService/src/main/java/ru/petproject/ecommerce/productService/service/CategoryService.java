package ru.petproject.ecommerce.productService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import ru.petproject.ecommerce.productService.dto.CategoryDto;
import ru.petproject.ecommerce.productService.exceptions.CategoryNotFoundException;
import ru.petproject.ecommerce.productService.exceptions.UserNotAuthException;
import ru.petproject.ecommerce.productService.model.Category;
import ru.petproject.ecommerce.productService.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import ru.petproject.ecommerce.productService.utils.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RestTemplate restTemplate;

    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDto> findCategoryById(Long id) {
        return categoryRepository.findByIdAndDeletedFalse(id)
                .map(this::convertToDTO);
    }

    private boolean isAdmin(String token) {
        String url = "http://user-service/isAdmin?token=" + token; //URL для запроса к userService
        Boolean response = restTemplate.getForObject(url, Boolean.class);
        return Boolean.TRUE.equals(response);
    }

    public CategoryDto createCategory(CategoryDto categoryDTO, String token) {
        String userId = jwtUtil.extractUserId(token);
        if (jwtUtil.isTokenValid(token) && isAdmin(token)) {
            Category category = convertToEntity(categoryDTO);
            Category savedCategory = categoryRepository.save(category);
            return convertToDTO(savedCategory);
        }
        throw new UserNotAuthException(userId);
    }

    public void deleteCategory(Long id, String token) {
        String userId = jwtUtil.extractUserId(token);
        if (jwtUtil.isTokenValid(token) && isAdmin(token)) {
            Category category = categoryRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new CategoryNotFoundException(id));
            category.setDeleted(true);
            categoryRepository.save(category);
        } else {
        throw new UserNotAuthException(userId);
        }
    }

    private CategoryDto convertToDTO(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.isDeleted()
        );
    }

    private Category convertToEntity(CategoryDto categoryDTO) {
        return new Category(
                categoryDTO.getId(),
                categoryDTO.getName(),
                categoryDTO.isDeleted()
        );
    }
}
