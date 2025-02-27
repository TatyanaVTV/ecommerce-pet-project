package com.aston_project.payment_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name("order-topic")
                .build();
    }

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name("payment-topic")
                .build();
    }
}