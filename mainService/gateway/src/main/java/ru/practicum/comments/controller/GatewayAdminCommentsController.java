package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.client.CommentsClient;
import ru.practicum.likeDto.LikeResponseDto;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class GatewayAdminCommentsController {

    private final CommentsClient client;

    @GetMapping("/{commentId}/like")
    @ResponseStatus(HttpStatus.OK)
    public List<LikeResponseDto> getCommentsLike(@PathVariable Long commentId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        return client.getCommentsLike(commentId, from, size);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getUserCommentsAdmin(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        return client.getUserCommentsAdmin(userId, from, size);
    }

    @DeleteMapping("{adminId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long adminId,
                              @PathVariable Long commentId,
                              @RequestParam(required = true) String reason) {
        client.deleteCommentByAdmin(adminId, commentId, reason);
    }

    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getDeletedComments(@RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return client.getDeletedComments(from, size);
    }

    @GetMapping("/deleted/{oldId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getDeletedCommentById(@PathVariable Long oldId) {
        return client.getDeletedCommentById(oldId);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CommentResponseDto> returnDeletedComments(@RequestParam(required = true) List<Long> ids) {
        return client.returnDeletedComments(ids);
    }
}
