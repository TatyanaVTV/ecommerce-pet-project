package ru.petproject.ecommerce.productService.repository;

import ru.petproject.ecommerce.productService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByDeletedFalse();
    Optional<Category> findByIdAndDeletedFalse(Long id);
}
