package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.client.EventClient;
import ru.practicum.eventsDto.EventFullDto;
import ru.practicum.eventsDto.UpdateEventAdminRequestDto;
import ru.practicum.requestParams.AdminGetEventsParam;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class GatewayAdminEventController {
    private final EventClient client;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEvents(@Valid AdminGetEventsParam getEventsParam) {
        return client.getAllEventsAdmin(getEventsParam);
    }


    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @Valid @RequestBody UpdateEventAdminRequestDto request) {
        return client.updateEventByAdmin(eventId, request);
    }
}
