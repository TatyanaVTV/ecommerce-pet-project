package ru.petproject.ecommerce.order_service.service.order;

import ru.petproject.ecommerce.order_service.dto.OrderDto;
import ru.petproject.ecommerce.order_service.dto.UpdateOrderDto;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    OrderDto createNewOrder(Long userId);
    OrderDto createOrderFromExist(OrderDto orderDto);
    OrderDto updateOrderTotalCost(Long orderId, UpdateOrderDto updateOrderDto);
    OrderDto updateOrderTotalCost(Long orderId, BigDecimal totalCost);
    void deleteOrder(Long orderId);
    OrderDto findOrderById(Long orderId);
    List<OrderDto> findOrdersByUserId(Long userId);
}
