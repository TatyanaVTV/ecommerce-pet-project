package com.aston_project.payment_service.controller;

import com.aston_project.payment_service.dto.PaymentDto;
import com.aston_project.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/payments")
public class PaymentController {

    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<PaymentDto> create(@RequestBody PaymentDto payment) {
        PaymentDto createdPayment = paymentService.create(payment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable(name = "id") Long id) {
        PaymentDto payment = paymentService.getPayment(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}