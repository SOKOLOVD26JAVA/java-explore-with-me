package ru.practicum.comments.mapper;

import ru.practicum.commentDto.CommentRequestDto;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.model.Comment;

public class CommentsMapper {
    public static Comment mapToComment(CommentRequestDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());

        return comment;
    }

    public static CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();

        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setEventId(comment.getEvent().getId());
        dto.setCreated(comment.getCreated());

        return dto;
    }


}
