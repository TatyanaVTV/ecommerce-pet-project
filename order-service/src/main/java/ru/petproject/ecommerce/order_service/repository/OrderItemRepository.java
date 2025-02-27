package ru.petproject.ecommerce.order_service.repository;

import ru.petproject.ecommerce.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderIdAndDeletedFalse(Long orderId);
    Optional<OrderItem> findByIdAndDeletedFalse(Long id);
}
