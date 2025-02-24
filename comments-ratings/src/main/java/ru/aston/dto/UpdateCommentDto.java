package ru.aston.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {
    @Size(min = 3, max = 2000)
    private String comment;
}
