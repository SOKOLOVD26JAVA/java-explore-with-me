package ru.practicum.eventsDto;

import lombok.Data;
import ru.practicum.LocationDto;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.usersDto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequest;
    private String description;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private LocalDateTime createdOn;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
