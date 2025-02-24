package com.aston_project.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String PAYMENT_TOPIC = "payment_topic";

    public void sendPayment(String event) {
        kafkaTemplate.send(PAYMENT_TOPIC, event);
    }
}