package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.client.EventClient;
import ru.practicum.eventsDto.EventFullDto;
import ru.practicum.eventsDto.EventShortDto;
import ru.practicum.requestParams.GetEventsParam;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class GatewayPublicEventController {

    private final EventClient client;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@Valid GetEventsParam getEventsParam) {
        return client.getEvents(getEventsParam);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable Long id) {
        return client.getEventById(id);
    }
}
