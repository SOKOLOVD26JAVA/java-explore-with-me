package ru.practicum.eventsDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.LocationDto;

import java.time.LocalDateTime;

@Data
public abstract class BaseUpdateEventRequest {
    @Size(min = 20, max = 2000, message = "Field: annotation. Error: length must be between 20 and 2000")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Field: description. Error: length must be between 20 and 7000")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero(message = "Field: participant_limit. Error: must be positive or zero.  Value: negative")
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120, message = "Field: title. Error: length must be between 3 and 120")
    private String title;
}
