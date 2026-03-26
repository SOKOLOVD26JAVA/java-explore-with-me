package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hitDto.HitDto;
import ru.practicum.hitDto.HitResponseDto;
import ru.practicum.service.StatsServerService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsServerController {

    private final StatsServerService service;

    @PostMapping("/hit")
    public HitDto saveHit(@RequestBody HitDto hitDto) {
        return service.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<HitResponseDto> getStats(@RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
