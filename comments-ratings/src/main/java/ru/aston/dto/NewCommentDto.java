package ru.aston.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @Size(min = 3, max = 2000)
    private String comment;

    private Integer rating;
}
