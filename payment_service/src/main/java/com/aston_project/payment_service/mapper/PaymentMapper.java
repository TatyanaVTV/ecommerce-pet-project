package com.aston_project.payment_service.mapper;

import com.aston_project.payment_service.dto.PaymentDto;
import com.aston_project.payment_service.entity.Payment;

public class PaymentMapper {

    public static PaymentDto toDTO(Payment payment) {
        return new PaymentDto(payment.getId(), payment.getOrderId(), payment.getSum(), payment.getStatus());
    }

    public static Payment toEntity(PaymentDto paymentDTO) {
        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());
        payment.setOrderId(paymentDTO.getOrderId());
        payment.setSum(paymentDTO.getSum());
        payment.setStatus(paymentDTO.getStatus());
        return payment;
    }
}
