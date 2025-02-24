package com.aston_project.payment_service.mapper;

import com.aston_project.payment_service.dto.PaymentDto;
import com.aston_project.payment_service.entity.Payment;

public class PaymentMapper {

    public static PaymentDto toDTO(Payment payment) {
        PaymentDto paymentDTO = new PaymentDto();
        paymentDTO.setId(payment.getId());
        paymentDTO.setOrderId(payment.getOrderId());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setSum(payment.getSum());
        paymentDTO.setPrice(payment.getPrice());
        return paymentDTO;
    }

    public static Payment toEntity(PaymentDto paymentDTO) {
        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());
        payment.setOrderId(paymentDTO.getOrderId());
        payment.setStatus(paymentDTO.getStatus());
        payment.setSum(paymentDTO.getSum());
        payment.setPrice(paymentDTO.getPrice());
        return payment;
    }
}