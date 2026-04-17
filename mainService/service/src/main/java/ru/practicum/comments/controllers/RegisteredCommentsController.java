package ru.practicum.comments.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commentDto.CommentRequestDto;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.service.CommentsService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class RegisteredCommentsController {
    private final CommentsService commentsService;

    @PostMapping("/{eventId}/users/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestBody CommentRequestDto dto,
                                            @PathVariable Long eventId,
                                            @PathVariable Long userId) {
        return commentsService.createComment(dto, eventId, userId);
    }

    @PatchMapping("/{eventId}/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateComment(@RequestBody CommentRequestDto dto,
                                            @PathVariable Long eventId,
                                            @PathVariable Long userId,
                                            @PathVariable Long commentId) {
        return commentsService.updateComment(dto, eventId, userId, commentId);
    }

    @DeleteMapping("/{eventId}/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long eventId,
                              @PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentsService.deleteComment(eventId, userId, commentId);
    }

    @GetMapping("/users/{userId}/event/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getUserComments(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return commentsService.getUserComments(userId, eventId, from, size);
    }
}
