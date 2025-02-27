package ru.aston.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String COMMENT_TOPIC = "comment-events";
    private static final String SCORE_TOPIC = "score-events";

    public void sendCommentEvent(String event) {
        kafkaTemplate.send(COMMENT_TOPIC, event);
    }

    public void sendScoreEvent(String event) {
        kafkaTemplate.send(SCORE_TOPIC, event);
    }
}