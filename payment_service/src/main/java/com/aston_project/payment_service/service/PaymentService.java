package com.aston_project.payment_service.service;

import com.aston_project.payment_service.entity.Payment;
import com.aston_project.payment_service.entity.PaymentStatus;

import java.util.List;

public interface PaymentService {

    public Long create(Payment payment);

    public PaymentStatus getPaymentStatus(Long id);

    public Payment getPayment(Long id);

    public void processPayment(Long id);
    //Отправляет запрос и в зависимости от ответа меняет его статус.


}
