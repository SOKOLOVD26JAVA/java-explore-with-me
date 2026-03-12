package ru.practicum.events.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.events.service.EventService;
import ru.practicum.eventsDto.EventFullDto;
import ru.practicum.eventsDto.State;
import ru.practicum.eventsDto.UpdateEventAdminRequestDto;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEvents(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                           @RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<State> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rageEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllEventsAdmin(adminId, users, states, categories, rangeStart, rageEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto updateEventByAdmin(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                           @PathVariable Long eventId,
                                           @RequestBody UpdateEventAdminRequestDto request) {
        return eventService.updateEventByAdmin(adminId, eventId, request);
    }
}
