package ru.practicum.eventsDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEventAdminRequestDto extends BaseUpdateEventRequest {
    @NotNull(message = "Field: stateAction. Error: must not be null. Value: null")
    private AdminStateAction stateAction;
}
