package ru.petproject.ecommerce.order_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.petproject.ecommerce.order_service.dto.OrderItemDto;
import ru.petproject.ecommerce.order_service.model.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "id", ignore = true)
    OrderItem toEntity(OrderItemDto orderItemDto);

    default OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .orderId(orderItem.getOrder().getId())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .productId(orderItem.getProductId())
                .deleted(orderItem.isDeleted())
                .build();
    }

    List<OrderItemDto> toDto(List<OrderItem> orderItems);
}
