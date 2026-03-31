package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commentDto.CommentRequestDto;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.client.CommentsClient;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class GatewayRegisteredCommentsController {

    private final CommentsClient client;

    @PostMapping("/{eventId}/users/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestBody CommentRequestDto dto,
                                            @PathVariable Long eventId,
                                            @PathVariable Long userId) {
        return client.createComment(dto, eventId, userId);
    }

    @PatchMapping("/{eventId}/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateComment(@RequestBody CommentRequestDto dto,
                                            @PathVariable Long eventId,
                                            @PathVariable Long userId,
                                            @PathVariable Long commentId) {
        return client.updateComment(dto, eventId, userId, commentId);
    }

    @DeleteMapping("/{eventId}/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long eventId,
                              @PathVariable Long userId,
                              @PathVariable Long commentId) {
        client.deleteComment(eventId, userId, commentId);
    }

    @GetMapping("/users/{userId}/event/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getUserComments(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return client.getUserComments(userId, eventId, from, size);
    }
}
