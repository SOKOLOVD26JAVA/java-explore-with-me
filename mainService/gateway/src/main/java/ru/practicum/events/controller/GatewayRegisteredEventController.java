package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.client.EventClient;
import ru.practicum.eventsDto.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class GatewayRegisteredEventController {

    private final EventClient client;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return client.getUserEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventWithOutPublishedOnDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto eventDto) {
        return client.createEvent(userId, eventDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return client.getUserEvent(userId, eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequestDto request) {
        return client.updateEventByUser(userId, eventId, request);
    }


}
