package ru.practicum.eventsDto;

import lombok.Data;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.usersDto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
