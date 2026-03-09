package ru.practicum.compilationsDto;

import lombok.Data;
import ru.practicum.eventsDto.EventShortDto;

import java.util.List;
@Data
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
