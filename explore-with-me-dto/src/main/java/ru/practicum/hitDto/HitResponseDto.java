package ru.practicum.hitDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class HitResponseDto {
    private String app;
    private String uri;
    private Long hits;
}
