package ru.petproject.ecommerce.order_service.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class UpdateOrderDto {
    private BigDecimal totalCost;
}
