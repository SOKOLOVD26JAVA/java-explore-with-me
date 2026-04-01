package ru.practicum.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.likeDto.LikeRequestDto;
import ru.practicum.likeDto.LikeResponseDto;
import ru.practicum.likes.client.LikesClient;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class GatewayRegisteredLikeController {

    private final LikesClient client;


    @PostMapping("/{userId}/comments/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeResponseDto addLike(@RequestBody LikeRequestDto dto,
                                   @PathVariable Long userId,
                                   @PathVariable Long commentId) {
        return client.addLike(dto, userId, commentId);
    }

    @PatchMapping("/{userId}/comments/{commentId}/like/{likeId}")
    @ResponseStatus(HttpStatus.OK)
    public LikeResponseDto updateLike(@RequestBody LikeRequestDto dto,
                                      @PathVariable Long userId,
                                      @PathVariable Long commentId,
                                      @PathVariable Long likeId) {
        return client.updateLike(dto, userId, commentId, likeId);
    }

    @DeleteMapping("/{userId}/comments/{commentId}/like/{likeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long userId,
                           @PathVariable Long commentId,
                           @PathVariable Long likeId) {

        client.deleteLike(userId, commentId, likeId);
    }
}
