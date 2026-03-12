package ru.practicum.eventsDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.LocationDto;

import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank(message = "Field: annotation. Error: must not be blank. Value: null")
    private String annotation;
    @NotNull(message = "Field: category. Error: must not be null. Value: null")
    private Long category;
    @NotBlank(message = "Field: description. Error: must not be blank. Value: null")
    private String description;
    @NotNull(message = "Field: eventDate. Error: must not be null. Value: null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Field: eventDate. Error: must be a future date and time.")
    private LocalDateTime eventDate;
    @NotNull(message = "Field: location. Error: must not be null.  Value: null")
    private LocationDto location;
    @NotNull(message = "Field: paid. Error: must not be null.  Value: null")
    private Boolean paid;
    @NotNull(message = "Field: participant_limit. Error: must not be null.  Value: null")
    private int participantLimit;
    @NotNull(message = "Field: request_moderation. Error: must not be null.  Value: null")
    private Boolean requestModeration;
    @NotNull(message = "Field: title. Error: must not be null.  Value: null")
    private String title;
}
