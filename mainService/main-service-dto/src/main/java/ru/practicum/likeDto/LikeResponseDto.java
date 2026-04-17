package ru.practicum.likeDto;

import lombok.Data;

@Data
public class LikeResponseDto {
    private Long id;
    private Integer rating;
    private Long commentId;
    private Long likerId;
}
