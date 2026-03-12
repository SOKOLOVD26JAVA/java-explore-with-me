package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.events.client.EventClient;
import ru.practicum.eventsDto.EventFullDto;
import ru.practicum.eventsDto.State;
import ru.practicum.eventsDto.UpdateEventAdminRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class GatewayAdminEventController {
    private final EventClient client;

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
        return client.getAllEventsAdmin(adminId, users, states, categories, rangeStart, rageEnd, from, size);
    }

    //    Валидация по ТЗ не требуется
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto updateEventByAdmin(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                           @PathVariable Long eventId,
                                           @Valid @RequestBody UpdateEventAdminRequestDto request) {
        return client.updateEventByAdmin(adminId, eventId, request);
    }
}
