package ru.practicum.requestsDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipantRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private EventRequestStatus status;
}
