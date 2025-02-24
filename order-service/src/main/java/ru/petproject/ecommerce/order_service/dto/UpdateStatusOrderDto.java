package ru.petproject.ecommerce.order_service.dto;

import lombok.Builder;
import lombok.Data;
import ru.petproject.ecommerce.order_service.model.Status;

@Data
@Builder
public class UpdateStatusOrderDto {
    private Status status;
}
