package ru.petproject.ecommerce.order_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.petproject.ecommerce.order_service.dto.OrderItemDto;
import ru.petproject.ecommerce.order_service.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    default OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .orderId(orderItem.getOrder().getId())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .productId(orderItem.getProductId())
                .deleted(orderItem.isDeleted())
                .build();
    }

    default List<OrderItemDto> toDto(List<OrderItem> orderItems) {
        List<OrderItemDto> orderItemDto = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDto.add(toOrderItemDto(orderItem));
        }
        return orderItemDto;
    }
}
