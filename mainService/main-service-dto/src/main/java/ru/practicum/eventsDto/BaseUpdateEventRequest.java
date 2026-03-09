package ru.practicum.eventsDto;

import lombok.Data;
import ru.practicum.LocationDto;

import java.time.LocalDateTime;

@Data
public abstract class BaseUpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
