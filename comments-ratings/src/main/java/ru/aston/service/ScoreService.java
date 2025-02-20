package ru.aston.service;

import ru.aston.dto.ScoreDto;

public interface ScoreService {

    ScoreDto getProductScore(Long productId);

    void rateProduct(Long userId, Long productId, int rating);

    void removeUserRating(Long userId, Long productId);
}