package ru.petproject.ecommerce.order_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.petproject.ecommerce.order_service.dto.OrderItemDto;
import ru.petproject.ecommerce.order_service.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "id", ignore = true)
    OrderItem toEntity(OrderItemDto orderItemDto);

    OrderItemDto toDto(OrderItem orderItem);
}
