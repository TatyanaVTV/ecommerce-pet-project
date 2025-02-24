package com.aston_project.payment_service.controller;

import com.aston_project.payment_service.entity.Payment;
import com.aston_project.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/payments")
public class PaymentController {

    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody Payment payment) {
        paymentService.create(payment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/process/{id}")
    public ResponseEntity<?> process(@PathVariable(name = "id") Long id) {
        paymentService.processPayment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
