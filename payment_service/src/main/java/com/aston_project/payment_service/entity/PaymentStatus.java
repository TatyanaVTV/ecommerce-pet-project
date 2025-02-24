package com.aston_project.payment_service.entity;

public enum PaymentStatus {
    NEW,
    CANCELLED_BY_CUSTOMER,
    CANCELLED_BY_BANK,
    ERROR,
    PROVIDED;
}
