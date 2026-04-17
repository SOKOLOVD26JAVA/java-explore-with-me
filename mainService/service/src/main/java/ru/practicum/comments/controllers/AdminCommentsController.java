package ru.practicum.comments.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.service.CommentsService;
import ru.practicum.likeDto.LikeResponseDto;
import ru.practicum.likes.service.LikeService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCommentsController {
    private final LikeService likeService;
    private final CommentsService commentsService;

    @GetMapping("/{commentId}/like")
    @ResponseStatus(HttpStatus.OK)
    public List<LikeResponseDto> getCommentsLike(@PathVariable Long commentId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        return likeService.getCommentLikes(commentId, from, size);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getUserCommentsAdmin(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        return commentsService.getUserCommentsAdmin(userId, from, size);
    }

    @DeleteMapping("{adminId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long adminId,
                              @PathVariable Long commentId,
                              @RequestParam(required = true) String reason) {
        commentsService.deleteCommentByAdmin(adminId, commentId, reason);
    }

    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getDeletedComments(@RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return commentsService.getDeletedComments(from, size);
    }

    @GetMapping("/deleted/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getDeletedCommentById(@PathVariable Long id) {
        return commentsService.getDeletedCommentById(id);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CommentResponseDto> returnDeletedComments(@RequestParam(required = true) List<Long> ids) {
        return commentsService.returnDeletedComments(ids);
    }


}
