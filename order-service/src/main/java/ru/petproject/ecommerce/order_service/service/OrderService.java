package ru.petproject.ecommerce.order_service.service;

import jakarta.transaction.Transactional;
import ru.petproject.ecommerce.order_service.dto.*;
import ru.petproject.ecommerce.order_service.model.Order;
import java.util.List;

public interface OrderService {
    @Transactional
    OrderDto findOrdersWithStatusNewByUserId(String token);
    @Transactional
    OrderItemDto findOrderItemById(Long orderItemId);
    @Transactional
    List<OrderItemDto> findOrderItemsByOrderId(Long orderId);
    @Transactional
    OrderDto addOrderItemToOrder(String token, OrderItemDtoWithoutOrderId dto);
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
    OrderDto findOrderById(Long orderId);
    @Transactional
    List<OrderDto> findOrdersByUserId(String token);
}
