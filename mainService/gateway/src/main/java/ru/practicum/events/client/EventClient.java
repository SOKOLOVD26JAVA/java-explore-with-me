package ru.practicum.events.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.eventsDto.*;
import ru.practicum.exceptions.GatewayException;
import ru.practicum.requestParams.AdminGetEventsParam;
import ru.practicum.requestParams.GetEventsParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventClient {

    private final RestTemplate restTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public List<EventFullDto> getAllEventsAdmin(AdminGetEventsParam param) {
        String baseUri = "/admin/events";
        StringBuilder uri = new StringBuilder();
        uri.append(baseUri);

        if (param.getUsers() != null && !param.getUsers().isEmpty()) {
            String strIds = param.getUsers().stream().map(String::valueOf).collect(Collectors.joining(","));
            uri.append("?users=").append(strIds);
        }
        if (param.getStates() != null && !param.getStates().isEmpty()) {
            String strStates = param.getStates().stream().map(String::valueOf).collect(Collectors.joining(","));
            if (uri.toString().contains("?")) {
                uri.append("&states=").append(strStates);
            } else {
                uri.append("?states=").append(strStates);
            }
        }
        if (param.getCategories() != null && !param.getCategories().isEmpty()) {
            String strCategories = param.getCategories().stream().map(String::valueOf).collect(Collectors.joining(","));
            if (uri.toString().contains("?")) {
                uri.append("&categories=").append(strCategories);
            } else {
                uri.append("?categories=").append(strCategories);
            }
        }
        if (param.getRangeStart() != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeStart=").append(param.getRangeStart().format(formatter));
            } else {
                uri.append("?rangeStart=").append(param.getRangeStart().format(formatter));
            }
        }
        if (param.getRangeEnd() != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeEnd=").append(param.getRangeEnd().format(formatter));
            } else {
                uri.append("?rangeEnd=").append(param.getRangeEnd().format(formatter));
            }
        }
        if (uri.toString().contains("?")) {
            uri.append("&from=").append(param.getFrom()).append("&size=").append(param.getSize());
        } else {
            uri.append("?from=").append(param.getFrom()).append("&size=").append(param.getSize());
        }
        String fullUri = createUrl(uri.toString());
        log.info(fullUri);

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

    public List<EventShortDto> getEvents(GetEventsParam param) {
        String baseUri = "/events";
        StringBuilder uri = new StringBuilder();
        uri.append(baseUri);

        if (param.getText() != null) {
            uri.append("?text=").append(param.getText());
        }
        if (param.getCategories() != null && !param.getCategories().isEmpty()) {
            String strStates = param.getCategories().stream().map(String::valueOf).collect(Collectors.joining(","));
            if (uri.toString().contains("?")) {
                uri.append("&categories=").append(strStates);
            } else {
                uri.append("?categories=").append(strStates);
            }
        }
        if (param.getPaid() != null) {
            if (uri.toString().contains("?")) {
                uri.append("&paid=").append(param.getPaid());
            } else {
                uri.append("?paid=").append(param.getPaid());
            }
        }
        if (param.getRangeStart() != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeStart=").append(param.getRangeStart().format(formatter));
            } else {
                uri.append("?rangeStart=").append(param.getRangeStart().format(formatter));
            }
        }
        if (param.getRangeEnd() != null) {
            if (uri.toString().contains("?")) {
                uri.append("&rangeEnd=").append(param.getRangeEnd().format(formatter));
            } else {
                uri.append("?rangeEnd=").append(param.getRangeEnd().format(formatter));
            }
        }
        if (param.getOnlyAvailable()) {
            if (uri.toString().contains("?")) {
                uri.append("&onlyAvailable=").append(param.getOnlyAvailable());
            } else {
                uri.append("?onlyAvailable=").append(param.getOnlyAvailable());
            }
        }
        if (param.getSort() != null) {
            if (uri.toString().contains("?")) {
                uri.append("&sort=").append(param.getSort());
            } else {
                uri.append("?sort=").append(param.getSort());
            }
        }
        if (uri.toString().contains("?")) {
            uri.append("&from=").append(param.getFrom()).append("&size=").append(param.getSize());
        } else {
            uri.append("?from=").append(param.getFrom()).append("&size=").append(param.getSize());
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

    private String createUrl(String path) {
        return serverUrl + path;
    }
}
