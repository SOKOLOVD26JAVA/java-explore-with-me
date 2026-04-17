package ru.practicum.commentDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private String text;
    private Long eventId;
    private Long authorId;
    private LocalDateTime created;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double rating;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reason;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deletedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long deletedBy;
}
