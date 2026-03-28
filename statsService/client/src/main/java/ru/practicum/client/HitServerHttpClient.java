package ru.practicum.client;

import ru.practicum.hitDto.HitDto;
import ru.practicum.hitDto.HitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitServerHttpClient {

    HitDto saveHit(String uri, String ip, LocalDateTime timestamp);

    List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
