package com.aston_project.payment_service.repository;

import com.aston_project.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //Добавить метод, который возвращает булиан если в базе есть пеймент с указанным orderId и
    //статусом "Оплачено"
}
