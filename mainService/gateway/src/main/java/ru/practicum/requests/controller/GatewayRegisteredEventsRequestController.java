package ru.practicum.requests.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.client.EventRequestClient;
import ru.practicum.requestsDto.EventRequestStatusUpdateRequest;
import ru.practicum.requestsDto.EventRequestStatusUpdateResult;
import ru.practicum.requestsDto.ParticipantRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GatewayRegisteredEventsRequestController {

    private final EventRequestClient client;

    @GetMapping("users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipantRequestDto> getUserRequests(@PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        return client.getUserRequests(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequest(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @Valid @RequestBody EventRequestStatusUpdateRequest dto) {
        return client.updateRequest(userId, eventId, dto);

    }

    @GetMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipantRequestDto> getCurrentUserRequests(@PathVariable Long userId) {
        return client.getCurrentUserRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipantRequestDto createRequestEvent(@PathVariable Long userId,
                                                    @RequestParam(required = true) Long eventId) {
        return client.createRequestEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipantRequestDto cancelRequest(@PathVariable Long userId,
                                               @PathVariable Long requestId) {
        return client.cancelRequest(userId, requestId);
    }
}
