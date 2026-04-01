package ru.practicum.likeDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LikeRequestDto {
    @Min(1)
    @Max(5)
    private Integer rating;
}
