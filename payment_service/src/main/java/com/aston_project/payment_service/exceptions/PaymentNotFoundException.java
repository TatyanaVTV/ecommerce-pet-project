package com.aston_project.payment_service.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Платеж с id " + id + " не найден");
    }
}
