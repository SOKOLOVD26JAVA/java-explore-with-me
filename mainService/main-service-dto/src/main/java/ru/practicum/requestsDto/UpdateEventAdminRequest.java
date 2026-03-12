package ru.practicum.requestsDto;

import lombok.Data;
import ru.practicum.LocationDto;


import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {

    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
