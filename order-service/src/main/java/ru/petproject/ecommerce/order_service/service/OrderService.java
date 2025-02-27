package ru.petproject.ecommerce.order_service.service;

import jakarta.transaction.Transactional;
import ru.petproject.ecommerce.order_service.dto.*;
import ru.petproject.ecommerce.order_service.model.Order;
import java.util.List;

public interface OrderService {
    @Transactional
    OrderDto findOrdersWithStatusNewByUserId(Long userId);
    @Transactional
    OrderItemDto findOrderItemById(Long orderItemId);
    @Transactional
    List<OrderItemDto> findOrderItemsByOrderId(Long orderId);
    @Transactional
    OrderDto addOrderItemToOrder(Long userId, OrderItemDtoWithoutOrderId dto);
    @Transactional
    Order createNewOrder(Long userId);
    @Transactional
    OrderItemDto changeOrderItemQuantity (Long orderItemId, UpdateOrderItemDto updateOrderItemDto);
    @Transactional
    void deleteOrderItem(Long orderItemId);
    @Transactional
    OrderDto placeOrder(Long orderId);
    @Transactional
    void deleteOrder(Long orderId);
    @Transactional
    OrderDto findOrderById(Long orderId);
    @Transactional
    List<OrderDto> findOrdersByUserId(Long userId);
}
