package ru.practicum.requests.client;

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
import ru.practicum.exceptions.GatewayException;
import ru.practicum.requestsDto.EventRequestStatusUpdateRequest;
import ru.practicum.requestsDto.EventRequestStatusUpdateResult;
import ru.practicum.requestsDto.ParticipantRequestDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventRequestClient {

    private final RestTemplate restTemplate;

    @Value("${explore.main.server.url}")
    private String serverUrl;


    public List<ParticipantRequestDto> getUserRequests(Long userId,
                                                       Long eventId) {
        String uri = createUrl("/users/" + userId + "/events/" + eventId + "/requests");
        try {
            ResponseEntity<List<ParticipantRequestDto>> response = restTemplate
                    .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<ParticipantRequestDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public EventRequestStatusUpdateResult updateRequest(Long userId,
                                                        Long eventId,
                                                        EventRequestStatusUpdateRequest dto) {
        String uri = createUrl("/users/" + userId + "/events/" + eventId + "/requests");
        HttpEntity<EventRequestStatusUpdateRequest> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<EventRequestStatusUpdateResult> response = restTemplate
                    .exchange(uri, HttpMethod.PATCH, request, EventRequestStatusUpdateResult.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<ParticipantRequestDto> getCurrentUserRequests(Long userId) {
        String uri = createUrl("/users/" + userId + "/requests");
        try {
            ResponseEntity<List<ParticipantRequestDto>> response = restTemplate
                    .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<ParticipantRequestDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public ParticipantRequestDto createRequestEvent(Long userId,
                                                    Long eventId) {
        String uri = createUrl("/users/" + userId + "/requests?eventId=" + eventId);
        try {
            ResponseEntity<ParticipantRequestDto> response = restTemplate
                    .exchange(uri, HttpMethod.POST, null, ParticipantRequestDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public ParticipantRequestDto cancelRequest(Long userId,
                                               Long requestId) {
        String uri = createUrl("/users/" + userId + "/requests/" + requestId + "/cancel");
        try {
            ResponseEntity<ParticipantRequestDto> response = restTemplate
                    .exchange(uri, HttpMethod.PATCH, null, ParticipantRequestDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    private String createUrl(String path) {
        return serverUrl + path;
    }
}
