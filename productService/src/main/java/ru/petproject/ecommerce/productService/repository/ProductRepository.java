package ru.petproject.ecommerce.productService.repository;

import ru.petproject.ecommerce.productService.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDeletedFalse();
    Optional<Product> findByIdAndDeletedFalse(Long id);
}

