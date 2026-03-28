package ru.practicum.requestsDto;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    List<ParticipantRequestDto> confirmedRequests;
    List<ParticipantRequestDto> rejectedRequests;
}
