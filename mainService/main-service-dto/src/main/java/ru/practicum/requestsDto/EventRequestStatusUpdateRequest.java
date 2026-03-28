package ru.practicum.requestsDto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    @NotNull(message = "Field: requests. Error: must not be null. Value: null")
    List<Long> requestIds;
    @NotNull(message = "Field: status. Error: must not be null. Value: null")
    RequestStatus status;
}
