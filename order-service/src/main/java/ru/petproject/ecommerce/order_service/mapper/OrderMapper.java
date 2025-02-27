package ru.petproject.ecommerce.order_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.petproject.ecommerce.order_service.dto.OrderDto;
import ru.petproject.ecommerce.order_service.model.Order;
import java.time.LocalDateTime;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    default OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .status(String.valueOf(order.getStatus()))
                .paymentMethod(order.getPaymentMethod())
                .totalCost(order.getTotalCost())
                .userId(order.getUserId())
                .createdAt(order.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .deleted(order.isDeleted())
                .items(OrderItemMapper.INSTANCE.toDto(order.getOrderItems()))
                .build();
    }
}
