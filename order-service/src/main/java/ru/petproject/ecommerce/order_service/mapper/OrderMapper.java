package ru.petproject.ecommerce.order_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.petproject.ecommerce.order_service.dto.OrderDto;
import ru.petproject.ecommerce.order_service.model.Order;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Order toEntity(OrderDto orderDto);

    OrderDto toDto(Order order);
}
