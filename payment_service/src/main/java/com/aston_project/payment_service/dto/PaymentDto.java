package com.aston_project.payment_service.dto;

import com.aston_project.payment_service.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private BigDecimal sum;

    @NotNull
    private PaymentStatus status;
}
