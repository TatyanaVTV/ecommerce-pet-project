package ru.petproject.ecommerce.order_service.repository;

import ru.petproject.ecommerce.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdAndDeletedFalse(Long userId);
    Optional<Order> findByIdAndDeletedFalse(Long id);
}
