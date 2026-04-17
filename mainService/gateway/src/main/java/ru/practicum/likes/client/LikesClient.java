package ru.practicum.likes.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.exceptions.GatewayException;
import ru.practicum.likeDto.LikeRequestDto;
import ru.practicum.likeDto.LikeResponseDto;

@Component
@RequiredArgsConstructor
public class LikesClient {

    private final RestTemplate restTemplate;

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public LikeResponseDto addLike(LikeRequestDto dto, Long userId, Long commentId) {
        String url = createUrl("/users/" + userId + "/comments/" + commentId + "/like");
        HttpEntity<LikeRequestDto> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<LikeResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, LikeResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public LikeResponseDto updateLike(LikeRequestDto dto, Long userId, Long commentId, Long likeId) {
        String url = createUrl("/users/" + userId + "/comments/" + commentId + "/like/" + likeId);
        HttpEntity<LikeRequestDto> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<LikeResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.PATCH, request, LikeResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public void deleteLike(Long userId, Long commentId, Long likeId) {
        String url = createUrl("/users/" + userId + "/comments/" + commentId + "/like/" + likeId);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    private String createUrl(String path) {
        return serverUrl + path;
    }
}
