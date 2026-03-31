package ru.practicum.comments.mapper;

import ru.practicum.commentDto.DeletedCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.DeletedComment;

public class DeletedCommentsMapper {

    public static DeletedCommentDto mapToDeletedCommentDto(DeletedComment comment) {
        DeletedCommentDto dto = new DeletedCommentDto();
        dto.setId(comment.getId());
        dto.setOldCommentId(comment.getOldCommentId());
        dto.setDeletedAt(comment.getDeletedAt());
        dto.setEventId(comment.getEvent().getId());
        dto.setText(comment.getText());
        dto.setAdminId(comment.getAdmin().getId());
        dto.setAuthorId(comment.getAuthor().getId());

        return dto;
    }

    public static Comment mapToCommentFromDeletedComment(DeletedComment deletedComment) {
        Comment comment = new Comment();

        comment.setText(deletedComment.getText());
        comment.setAuthor(deletedComment.getAuthor());
        comment.setEvent(deletedComment.getEvent());
        comment.setCreated(deletedComment.getCreated());

        return comment;
    }
}
