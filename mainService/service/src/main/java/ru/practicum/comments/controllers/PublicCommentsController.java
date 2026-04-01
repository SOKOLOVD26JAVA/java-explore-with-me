package ru.practicum.comments.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.service.CommentsService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class PublicCommentsController {
    private final CommentsService commentsService;

    @GetMapping("/{eventId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getAllCommentsEvent(@PathVariable Long eventId,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return commentsService.getAllCommentsEvent(eventId, sort, from, size);
    }

    @GetMapping("/{eventId}/search")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentEventSearch(@PathVariable Long eventId,
                                                          @RequestParam(required = true) String text,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {
        return commentsService.getCommentEventSearch(eventId, text, from, size);
    }

}
