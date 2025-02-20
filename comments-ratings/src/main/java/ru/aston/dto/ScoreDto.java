package ru.aston.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {

    public ScoreDto(Long productId, Double averageScore) {
        this.productId = productId;
        this.averageScore = averageScore;
    }

    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Long userId;

    @NotNull
    private Integer rating;

    private Double averageScore;
}