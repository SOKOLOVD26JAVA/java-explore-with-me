package ru.practicum.eventsDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdateEventUserRequestDto extends BaseUpdateEventRequest {

    @NotNull(message = "Field: stateAction. Error: must not be null. Value: null")
    private UserStateAction stateAction;
}
