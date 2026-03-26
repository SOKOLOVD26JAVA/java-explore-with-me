package ru.practicum.eventsDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.LocationDto;

import java.time.LocalDateTime;

@Data
public class NewEventDto {

    @NotBlank(message = "Field: annotation. Error: must not be blank. Value: null")
    @Size(min = 20, max = 2000, message = "Field: annotation. Error: length must be between 20 and 2000")
    private String annotation;
    @NotNull(message = "Field: category. Error: must not be null. Value: null")
    private Long category;
    @Size(min = 20, max = 7000, message = "Field: description. Error: length must be between 20 and 7000")
    @NotBlank(message = "Field: description. Error: must not be blank. Value: null")
    private String description;
    @NotNull(message = "Field: eventDate. Error: must not be null. Value: null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(message = "Field: location. Error: must not be null.  Value: null")
    private LocationDto location;
    private Boolean paid;
    @Min(value = 0, message = "Field: participant_limit. Error: must be greater than or equal to 0")
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(message = "Field: title. Error: must not be null.  Value: null")
    @Size(min = 3, max = 120, message = "Field: title. Error: length must be between 3 and 120")
    private String title;
}
