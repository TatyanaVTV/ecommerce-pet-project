package ru.petproject.ecommerce.order_service.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderDto {
    private Long userId;
    private String status;
    private BigDecimal totalCost;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
