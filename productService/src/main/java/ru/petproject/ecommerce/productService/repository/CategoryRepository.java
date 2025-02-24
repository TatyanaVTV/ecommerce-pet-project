package ru.petproject.ecommerce.productService.repository;

import ru.petproject.ecommerce.productService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
