package ru.petproject.ecommerce.order_service.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDto {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private boolean deleted;
}
