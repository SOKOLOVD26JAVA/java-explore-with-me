package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.eventsDto.State;
import ru.practicum.exceptions.AccessException;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.requestsDto.EventRequestStatus;
import ru.practicum.requestsDto.EventRequestStatusUpdateRequest;
import ru.practicum.requestsDto.EventRequestStatusUpdateResult;
import ru.practicum.requestsDto.ParticipantRequestDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestsRepository requestsRepository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    public List<ParticipantRequestDto> getUserRequests(Long userId, Long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        if (event.getInitiator().getId() != user.getId()) {
            throw new AccessException("You have no access for this operation.");
        }

        return requestsRepository.findAllByEventId(eventId).stream().map(RequestMapper::matToRequestDto).collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult updateRequest(Long userId,
                                                        Long eventId,
                                                        EventRequestStatusUpdateRequest dto) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new AccessException("You have no access for this operation.");
        }

        List<Request> requests = requestsRepository.findAllById(dto.getRequests());

        checkRequestStatus(requests);
        checkConfirmedRequestLimitException(event);
        switch (dto.getStatus()) {
            case CONFIRMED -> {
                if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                    for (Request request : requests) {
                        request.setStatus(EventRequestStatus.CONFIRMED);
                    }
                    return RequestMapper.mapToRequestResult(requests);
                } else {
                    Long count = requestsRepository.getConfirmedRequestsCount(eventId);
                    int limit = event.getParticipantLimit();

                    for (Request request : requests) {
                        if (limit > count) {
                            request.setStatus(EventRequestStatus.CONFIRMED);
                            count++;
                        } else {
                            request.setStatus(EventRequestStatus.REJECTED);
                        }
                    }
                    return RequestMapper.mapToRequestResult(requests);
                }
            }
            case REJECTED -> {
                for (Request request : requests) {
                    request.setStatus(EventRequestStatus.REJECTED);
                }
                return RequestMapper.mapToRequestResult(requests);
            }
            default -> throw new BadRequestException("incorrect field 'status'. ");
        }
    }

    public List<ParticipantRequestDto> getCurrentUserRequests(Long userId) {
        User user = getUserById(userId);
        return requestsRepository.findAllByRequesterId(userId).stream().map(RequestMapper::matToRequestDto).collect(Collectors.toList());
    }

    public ParticipantRequestDto createRequestEvent(Long userId, Long eventId) {
        Event event = getEventById(eventId);
        User user = getUserById(userId);
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Initiator cant create request.");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Event with ID = " + eventId + ", not found.");
        }

        checkConfirmedRequestLimitException(event);

        for (Request request : event.getRequests()) {
            if (request.getRequester().equals(user)) {
                throw new ConflictException("Your request has been existing");
            }
        }

        Request request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(EventRequestStatus.CONFIRMED);
        } else {
            request.setStatus(EventRequestStatus.PENDING);
        }

        return RequestMapper.matToRequestDto(requestsRepository.save(request));

    }

    public ParticipantRequestDto cancelRequest(Long userId,
                                               Long requestId) {
        User user = getUserById(userId);
        Request request = getRequestById(requestId);

        if (!user.getId().equals(request.getRequester().getId())) {
            throw new AccessException("You have no access for this operation.");
        }
        request.setStatus(EventRequestStatus.CANCELED);

        return RequestMapper.matToRequestDto(request);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + ", not found."));
    }

    private Event getEventById(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID = " + eventId + ", not found."));
    }

    private void checkRequestStatus(List<Request> requests) {
        for (Request request : requests) {
            if (!request.getStatus().equals(EventRequestStatus.PENDING)) {
                throw new ConflictException("Request must have status PENDING");
            }
        }
    }

    private Request getRequestById(Long requestId) {
        return requestsRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with ID = " + requestId + ", not found."));
    }

    private void checkConfirmedRequestLimitException(Event event) {
        if (requestsRepository.getConfirmedRequestsCount(event.getId()) >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached");
        }
    }
}
