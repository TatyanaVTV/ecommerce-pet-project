package com.aston_project.payment_service.service;

import com.aston_project.payment_service.dto.PaymentDto;

public interface PaymentService {

    PaymentDto create(PaymentDto paymentDTO);

    PaymentDto getPayment(Long id);

    void processPayment(Long id);
}