package ru.aston.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aston.dto.ScoreDto;
import ru.aston.entity.Score;
import ru.aston.exception.ForbiddenException;
import ru.aston.kafka.KafkaProducerService;
import ru.aston.repository.ScoreRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public ScoreDto getProductScore(Long productId) {
        log.info("Получение оценки для продукта с ID: {}", productId);
        List<Score> scores = scoreRepository.findByProductId(productId);
        if (scores.isEmpty()) {
            throw new ForbiddenException("Оценки для продукта с ID: " + productId + " не найдены");
        }
        double averageScore = scores.stream().mapToInt(Score::getRating).average().orElse(0.0);
        return new ScoreDto(productId, averageScore);
    }

    @Override
    @Transactional
    public void rateProduct(Long userId, Long productId, int rating) {
        log.info("Оценка продукта {} пользователем {} с рейтингом {}", productId, userId, rating);
        Optional<Score> optionalScore = scoreRepository.findByUserIdAndProductId(userId, productId);
        Score score = optionalScore.orElseGet(() -> {
            Score newScore = new Score();
            newScore.setProductId(productId);
            newScore.setUserId(userId);
            return newScore;
        });
        score.setRating(rating);
        scoreRepository.save(score);

        kafkaProducerService.sendScoreEvent("New rating: Product " + productId + " by User " + userId + " - " + rating);

        log.info("Продукт {} успешно оценен пользователем {}", productId, userId);
    }

    @Override
    @Transactional
    public void removeUserRating(Long userId, Long productId) {
        log.info("Удаление оценки для продукта {} пользователем {}", productId, userId);
        if (!scoreRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new ForbiddenException("Оценка не найдена для пользователя с ID: " + userId + " и продукта с ID: " + productId);
        }
        scoreRepository.deleteByUserIdAndProductId(userId, productId);
        log.info("Оценка успешно удалена для продукта {} пользователем {}", productId, userId);
    }
}