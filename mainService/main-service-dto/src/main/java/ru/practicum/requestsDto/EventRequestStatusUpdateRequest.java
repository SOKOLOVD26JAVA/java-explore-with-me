package ru.practicum.requestsDto;

import lombok.Data;

import java.util.List;
@Data
public class EventRequestStatusUpdateRequest {
    List<Long> requests;
    RequestStatus status;
}
