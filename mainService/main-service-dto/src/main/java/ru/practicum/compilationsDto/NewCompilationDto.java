package ru.practicum.compilationsDto;

import lombok.Data;
import ru.practicum.eventsDto.EventShortDto;

import java.util.List;
@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
