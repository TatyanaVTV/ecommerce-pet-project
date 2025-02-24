package com.aston_project.payment_service.service;

import com.aston_project.payment_service.dto.PaymentDto;
import com.aston_project.payment_service.entity.Payment;
import com.aston_project.payment_service.entity.PaymentStatus;
import com.aston_project.payment_service.exceptions.PaymentNotFoundException;
import com.aston_project.payment_service.mapper.PaymentMapper;
import com.aston_project.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto create(PaymentDto paymentDTO) {
        log.info("Creating new payment with id: {}", paymentDTO.getId());

        Payment payment = PaymentMapper.toEntity(paymentDTO);
        processPayment(payment.getOrderId());
        Payment savedPayment = paymentRepository.save(payment);
        PaymentDto savedPaymentDTO = PaymentMapper.toDTO(savedPayment);

        log.info("Payment with id: {} has been created", savedPaymentDTO);
        return savedPaymentDTO;
    }

    @Override
    public PaymentDto getPayment(Long id) {
        log.info("Getting payment with id: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
        PaymentDto paymentDTO = PaymentMapper.toDTO(payment);

        log.info("Payment with id: {} has been retrieved", paymentDTO);
        return paymentDTO;
    }

    @Override
    public void processPayment(Long id) {
        log.info("Processing payment with id: {}", id);

        PaymentDto payment = getPayment(id);
        payment.setStatus(PaymentStatus.PROVIDED);
        Payment savedPayment = PaymentMapper.toEntity(payment);
        paymentRepository.save(savedPayment);

        log.info("Payment with id: {} has been processed", id);
    }
}