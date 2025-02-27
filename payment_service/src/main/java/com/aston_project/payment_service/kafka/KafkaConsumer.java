package com.aston_project.payment_service.kafka;

import com.aston_project.payment_service.controller.PaymentController;
import com.aston_project.payment_service.dto.PaymentDto;
import com.aston_project.payment_service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class KafkaConsumer {

    @Autowired
    private final PaymentService paymentService;

    @Autowired
    private final KafkaProducer kafkaProducer;

    public KafkaConsumer(PaymentService paymentService, KafkaProducer kafkaProducer) {
        this.paymentService = paymentService;
        this.kafkaProducer = kafkaProducer;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "order-topic", groupId = "user")
    public void consume(String message) {
        String orderId = message.split(";")[0];
        String totalSum = message.split(";")[1];

        LOGGER.info("Payment with order id "
                + orderId + "with the total sum "
                + totalSum + "has been received and is ready for processing.");

        Long parsedOrderId = Long.parseLong(orderId);
        boolean statusToReturn = paymentService.getPayment(parsedOrderId).getStatus();

        paymentService.processPayment(parsedOrderId);

        kafkaProducer.sendPaymentStatus(orderId + ";" + statusToReturn);
    }
}