package ru.practicum.requests.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.service.RequestService;
import ru.practicum.requestsDto.EventRequestStatusUpdateRequest;
import ru.practicum.requestsDto.EventRequestStatusUpdateResult;
import ru.practicum.requestsDto.ParticipantRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegisteredEventsRequestController {
    private final RequestService requestService;

    @GetMapping("{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipantRequestDto> getUserRequests(@PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        return requestService.getUserRequests(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestStatusUpdateResult updateRequest(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest dto) {
        return requestService.updateRequest(userId, eventId, dto);

    }

    @GetMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipantRequestDto> getCurrentUserRequests(@PathVariable Long userId) {
        return requestService.getCurrentUserRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipantRequestDto createRequestEvent(@PathVariable Long userId,
                                                    @RequestParam(required = true) Long eventId) {
        return requestService.createRequestEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipantRequestDto cancelRequest(@PathVariable Long userId,
                                               @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

}
