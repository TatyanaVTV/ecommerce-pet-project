package ru.petproject.ecommerce.order_service.service.orderitem;

import ru.petproject.ecommerce.order_service.dto.OrderItemDto;
import ru.petproject.ecommerce.order_service.dto.UpdateOrderItemDto;
import java.util.List;

public interface OrderItemService {
    OrderItemDto createOrderItem(OrderItemDto orderDto);
    OrderItemDto updateOrderItem(Long orderItemId, UpdateOrderItemDto updateOrderItemDto);
    void deleteOrderItem(Long orderItemId);
    OrderItemDto findOrderItemById(Long orderItemId);
    List<OrderItemDto> findOrderItemsByOrderId(Long orderId);
}
