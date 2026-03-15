package ru.practicum.events.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.*;
import org.springframework.stereotype.Component;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import ru.practicum.eventsDto.*;
import ru.practicum.exceptions.GatewayException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public List<EventFullDto> getAllEventsAdmin(List<Long> ids,
                                                List<State> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rageEnd,
                                                int from,
                                                int size) {
        String baseUri = "/admin/events";
        StringBuilder uri = new StringBuilder();
        uri.append(baseUri);

        if (ids != null && !ids.isEmpty()) {
            String strIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
            uri.append("?users=").append(strIds);
        }
        if (states != null && !states.isEmpty()) {
            String strStates = states.stream().map(String::valueOf).collect(Collectors.joining(","));
            if (uri.toString().contains("?")) {
                uri.append("&states=").append(strStates);
            } else {
                uri.append("?states=").append(strStates);
            }
        }
        if (categories != null && !categories.isEmpty()) {
            String strCategories = categories.stream().map(String::valueOf).collect(Collectors.joining(","));
            if (uri.toString().contains("?")) {
                uri.append("&categories=").append(strCategories);
            } else {
                uri.append("?categories=").append(strCategories);
            }
        }
        if (rangeStart != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeStart=").append(rangeStart);
            } else {
                uri.append("?rangeStart=").append(rangeStart);
            }
        }
        if (rageEnd != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeEnd=").append(rageEnd);
            } else {
                uri.append("?rangeEnd=").append(rageEnd);
            }
        }
        if (uri.toString().contains("?")) {
            uri.append("&from=").append(from).append("&size=").append(size);
        } else {
            uri.append("?from=").append(from).append("&size=").append(size);
        }
        String fullUri = createUrl(uri.toString());

        try {
            ResponseEntity<List<EventFullDto>> response = restTemplate
                    .exchange(fullUri, HttpMethod.GET, null, new ParameterizedTypeReference<List<EventFullDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public EventFullDto updateEventByAdmin(Long eventId,
                                           UpdateEventAdminRequestDto dto) {
        String uri = createUrl("/admin/events/" + eventId);
        HttpEntity<UpdateEventAdminRequestDto> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<EventFullDto> response = restTemplate
                    .exchange(uri, HttpMethod.PATCH, request, EventFullDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }

    }

    public List<EventShortDto> getEvents(String text,
                                         List<Long> catIds,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rageEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         int from,
                                         int size
    ) {
        String baseUri = "/events";
        StringBuilder uri = new StringBuilder();
        uri.append(baseUri);

        if (text != null) {
            uri.append("?text=").append(text);
        }
        if (catIds != null && !catIds.isEmpty()) {
            String strStates = catIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            if (uri.toString().contains("?")) {
                uri.append("&categories=").append(strStates);
            } else {
                uri.append("?categories=").append(strStates);
            }
        }
        if (paid != null) {
            if (uri.toString().contains("?")) {
                uri.append("&paid=").append(paid);
            } else {
                uri.append("?paid=").append(paid);
            }
        }
        if (rangeStart != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeStart=").append(rangeStart);
            } else {
                uri.append("?rangeStart=").append(rangeStart);
            }
        }
        if (rageEnd != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeEnd=").append(rageEnd);
            } else {
                uri.append("?rangeEnd=").append(rageEnd);
            }
        }
        if (onlyAvailable) {
            if (uri.toString().contains("?")) {
                uri.append("&onlyAvailable=").append(onlyAvailable);
            } else {
                uri.append("?onlyAvailable=").append(onlyAvailable);
            }
        }
        if (sort != null) {
            if (uri.toString().contains("?")) {
                uri.append("&sort=").append(sort);
            } else {
                uri.append("?sort=").append(sort);
            }
        }
        if (uri.toString().contains("?")) {
            uri.append("&from=").append(from).append("&size=").append(size);
        } else {
            uri.append("?from=").append(from).append("&size=").append(size);
        }

        String fullUri = createUrl(uri.toString());
        try {
            ResponseEntity<List<EventShortDto>> response = restTemplate
                    .exchange(fullUri, HttpMethod.GET, null, new ParameterizedTypeReference<List<EventShortDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public EventFullDto getEventById(Long id) {
        String uri = createUrl("/events/" + id);
        try {
            ResponseEntity<EventFullDto> response = restTemplate
                    .exchange(uri, HttpMethod.GET, null, EventFullDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }


    public List<EventShortDto> getUserEvents(Long userID,
                                             int from,
                                             int size) {
        String uri = createUrl("/users/" + userID + "/events?from=" + from + "&size=" + size);
        try {
            ResponseEntity<List<EventShortDto>> response = restTemplate
                    .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<EventShortDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public EventWithOutPublishedOnDto createEvent(Long userId, NewEventDto eventDto) {
        String uri = createUrl("/users/" + userId + "/events");
        HttpEntity<NewEventDto> request = new HttpEntity<>(eventDto);
        try {
            ResponseEntity<EventWithOutPublishedOnDto> response = restTemplate
                    .exchange(uri, HttpMethod.POST, request, EventWithOutPublishedOnDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public EventFullDto getUserEvent(Long userId, Long eventId) {
        String uri = createUrl("/users/" + userId + "/events/" + eventId);
        try {
            ResponseEntity<EventFullDto> response = restTemplate.exchange(uri, HttpMethod.GET, null, EventFullDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public EventFullDto updateEventByUser(Long userId,
                                          Long eventId,
                                          UpdateEventUserRequestDto requestDto) {
        String uri = createUrl("/users/" + userId + "/events/" + eventId);
        HttpEntity<UpdateEventUserRequestDto> request = new HttpEntity<>(requestDto);
        try {
            ResponseEntity<EventFullDto> response = restTemplate
                    .exchange(uri, HttpMethod.PATCH, request, EventFullDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.ALL));

        return headers;
    }

    private HttpHeaders createHeadersWithUserId(Long userId) {
        HttpHeaders headers = createHeaders();
        headers.set("X-Sharer-User-Id", userId.toString());
        return headers;
    }

    private String createUrl(String path) {
        return serverUrl + path;
    }
}
