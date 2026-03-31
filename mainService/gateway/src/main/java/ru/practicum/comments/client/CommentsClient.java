package ru.practicum.comments.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.commentDto.CommentRequestDto;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.commentDto.DeletedCommentDto;
import ru.practicum.exceptions.GatewayException;
import ru.practicum.likeDto.LikeResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentsClient {

    private final RestTemplate restTemplate;

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public List<LikeResponseDto> getCommentsLike(Long commentId,
                                                 int from,
                                                 int size) {
        String url = createUrl("/admin/" + commentId + "/like?from=" + from + "&size=" + size);

        try {
            ResponseEntity<List<LikeResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<LikeResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<CommentResponseDto> getUserCommentsAdmin(Long userId,
                                                         int from,
                                                         int size) {
        String url = createUrl("/admin/users/" + userId + "?from=" + from + "&size=" + size);

        try {
            ResponseEntity<List<CommentResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public void deleteCommentByAdmin(Long adminId,
                                     Long commentId,
                                     String reason) {
        String url = createUrl("/admin/" + adminId + "/comments/" + commentId + "?reason=" + reason);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<DeletedCommentDto> getDeletedComments(int from,
                                                      int size) {
        String url = createUrl("/admin/deleted?from=" + from + "&size=" + size);

        try {
            ResponseEntity<List<DeletedCommentDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<DeletedCommentDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public DeletedCommentDto getDeletedCommentByOldId(Long oldId) {
        String url = createUrl("/admin/deleted/" + oldId);

        try {
            ResponseEntity<DeletedCommentDto> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, DeletedCommentDto.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<CommentResponseDto> returnDeletedComments(List<Long> ids) {

        String strIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        String url = createUrl("/admin/return?ids=" + strIds);
        try {
            ResponseEntity<List<CommentResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<List<CommentResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<CommentResponseDto> getAllCommentsEvent(Long eventId, String sort, int from, int size) {

        StringBuilder urlBuilder = new StringBuilder(createUrl("/comments/" + eventId + "/all?from=" + from + "&size=" + size));

        if (sort != null && !sort.isBlank()) {
            urlBuilder.append("&sort=").append(sort);
        }

        String url = urlBuilder.toString();

        try {
            ResponseEntity<List<CommentResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<CommentResponseDto> getCommentEventSearch(Long eventId, String text, int from, int size) {
        String url = createUrl("/comments/" + eventId + "/search?text=" + text + "&from=" + from + "&size=" + size);

        try {
            ResponseEntity<List<CommentResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public CommentResponseDto createComment(CommentRequestDto dto, Long eventId, Long userId) {
        String url = createUrl("/events/" + eventId + "/users/" + userId + "/comments");
        HttpEntity<CommentRequestDto> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<CommentResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, CommentResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public CommentResponseDto updateComment(CommentRequestDto dto, Long eventId, Long userId, Long commentId) {
        String url = createUrl("/events/" + eventId + "/users/" + userId + "/comments/" + commentId);
        HttpEntity<CommentRequestDto> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<CommentResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.PATCH, request, CommentResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public void deleteComment(Long eventId, Long userId, Long commentId) {
        String url = createUrl("/events/" + eventId + "/users/" + userId + "/comments/" + commentId);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<CommentResponseDto> getUserComments(Long userId, Long eventId, int from, int size) {
        String url = createUrl("/events/" + eventId + "/users/" + userId + "/comments?from=" + from + "&size=" + size);
        try {
            ResponseEntity<List<CommentResponseDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }


    private String createUrl(String path) {
        return serverUrl + path;
    }
}
