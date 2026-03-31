package ru.practicum.commentDto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeletedCommentDto {
    private Long id;
    private Long oldCommentId;
    private Long eventId;
    private Long authorId;
    private Long adminId;
    private LocalDateTime deletedAt;
    private String text;

}
