package com.aston_project.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String PAYMENT_TOPIC = "payment_topic";

    public void sendPaymentStatus(String message) {
        LOGGER.info("Payment with order id ["
                + message.split(";")[0]
                + "] has been granted status: "
                + message.split(";")[1] + ".");

        kafkaTemplate.send(PAYMENT_TOPIC, message);
    }
}
