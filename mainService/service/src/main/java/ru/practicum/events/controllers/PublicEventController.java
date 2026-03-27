package ru.practicum.events.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.service.EventService;
import ru.practicum.eventsDto.EventFullDto;
import ru.practicum.eventsDto.EventShortDto;
import ru.practicum.requestParams.GetEventsParam;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@Valid GetEventsParam getEventsParam, HttpServletRequest request) {
        return eventService.getEvents(getEventsParam, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable Long id,
                                     HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }
}
