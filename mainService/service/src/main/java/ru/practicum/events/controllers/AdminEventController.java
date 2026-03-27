package ru.practicum.events.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.service.EventService;
import ru.practicum.eventsDto.EventFullDto;
import ru.practicum.eventsDto.UpdateEventAdminRequestDto;
import ru.practicum.requestParams.AdminGetEventsParam;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEvents(AdminGetEventsParam getEventsParam) {
        return eventService.getAllEventsAdmin(getEventsParam);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody UpdateEventAdminRequestDto request) {
        return eventService.updateEventByAdmin(eventId, request);
    }
}
