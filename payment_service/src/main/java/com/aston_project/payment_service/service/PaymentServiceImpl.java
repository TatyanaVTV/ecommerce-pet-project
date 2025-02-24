package com.aston_project.payment_service.service;

import com.aston_project.payment_service.dto.PaymentDTO;
import com.aston_project.payment_service.entity.Payment;
import com.aston_project.payment_service.entity.PaymentStatus;
import com.aston_project.payment_service.exceptions.PaymentNotFoundException;
import com.aston_project.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

    @Override
    public Long create(Payment payment) {
        Payment savedPayment = paymentRepository.save(payment);
        return savedPayment.getId();
    }

    @Override
    public Payment getPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public PaymentStatus getPaymentStatus(Long id) {
        return getPayment(id).getStatus();
    }

    @Override
    public void processPayment(Long id) {
        Payment payment = getPayment(id);
        log.info("Payment request has been sent to external SBP Service API.");
        payment.setStatus(PaymentStatus.PROVIDED);
        paymentRepository.save(payment);
    }

    public PaymentDTO paymentToDto(Payment payment) {
        return new PaymentDTO(payment.getOrderId(), payment.getStatus());
    }

//    public Payment dtoToPayment(PaymentDTO paymentDTO) {
//    }






}
