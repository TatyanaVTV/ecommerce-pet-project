package com.aston_project.payment_service.mapper;

import com.aston_project.payment_service.dto.PaymentDto;
import com.aston_project.payment_service.entity.Payment;

public class PaymentMapper {

    public static PaymentDto toDTO(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setOrderId(payment.getOrderId());
        paymentDto.setSum(payment.getSum());
        paymentDto.setStatus(payment.getStatus());
        return paymentDto;
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
