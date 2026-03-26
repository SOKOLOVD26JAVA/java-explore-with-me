package ru.practicum.requests.mapper;

import ru.practicum.requests.model.Request;
import ru.practicum.requestsDto.EventRequestStatus;
import ru.practicum.requestsDto.EventRequestStatusUpdateResult;
import ru.practicum.requestsDto.ParticipantRequestDto;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static ParticipantRequestDto matToRequestDto(Request request) {
        ParticipantRequestDto dto = new ParticipantRequestDto();

        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setCreated(request.getCreated());
        dto.setEvent(request.getEvent().getId());
        dto.setStatus(request.getStatus());

        return dto;
    }

    public static EventRequestStatusUpdateResult mapToRequestResult(List<Request> requests) {

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(requests.stream()
                .filter(request -> request.getStatus().equals(EventRequestStatus.CONFIRMED)).toList()
                .stream().map(RequestMapper::matToRequestDto).collect(Collectors.toList()));

        eventRequestStatusUpdateResult.setRejectedRequests(requests.stream()
                .filter(request -> request.getStatus().equals(EventRequestStatus.REJECTED)).toList()
                .stream().map(RequestMapper::matToRequestDto).collect(Collectors.toList()));

        return eventRequestStatusUpdateResult;
    }
}
