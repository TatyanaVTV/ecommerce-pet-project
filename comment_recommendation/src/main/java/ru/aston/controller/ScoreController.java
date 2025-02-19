package ru.aston.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.dto.ScoreDto;
import ru.aston.service.ScoreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{prodId}/score")
public class ScoreController {
    private final ScoreService scoreService;

    @GetMapping
    public ScoreDto getScoreByProductId(@PathVariable Long prodId) {
        return scoreService.getProductScore(prodId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ScoreDto updateScore(
            @PathVariable Long prodId,
            @RequestParam Long userId,
            @RequestParam int rating) {
        scoreService.rateProduct(userId, prodId, rating);
        return scoreService.getProductScore(prodId);
    }

    @DeleteMapping
    public void deleteScore(@PathVariable Long prodId, @RequestParam Long userId) {
        scoreService.removeUserRating(userId, prodId);
    }
}