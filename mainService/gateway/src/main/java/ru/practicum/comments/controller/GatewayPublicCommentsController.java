package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.client.CommentsClient;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class GatewayPublicCommentsController {
    private final CommentsClient client;

    @GetMapping("/{eventId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getAllCommentsEvent(@PathVariable Long eventId,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return client.getAllCommentsEvent(eventId, sort, from, size);
    }

    @GetMapping("/{eventId}/search")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentEventSearch(@PathVariable Long eventId,
                                                          @RequestParam(required = true) String text,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {
        return client.getCommentEventSearch(eventId, text, from, size);
    }
}
