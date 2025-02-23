package ru.petproject.ecommerce.order_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateOrderItemDto {
    private Long orderId;
    private Integer quantity;
}
