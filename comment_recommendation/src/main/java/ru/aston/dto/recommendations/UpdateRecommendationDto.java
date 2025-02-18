package ru.aston.dto.recommendations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecommendationDto {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer rating;

    private Double score;
}