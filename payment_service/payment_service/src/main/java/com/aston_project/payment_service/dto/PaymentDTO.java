package com.aston_project.payment_service.dto;

import com.aston_project.payment_service.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long orderId;
    private PaymentStatus status;
}
