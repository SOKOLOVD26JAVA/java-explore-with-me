package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.client.HitServerHttpClientImpl;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventWithConfirmed;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.eventsDto.*;
import ru.practicum.exceptions.*;
import ru.practicum.hitDto.HitResponseDto;
import ru.practicum.locationModel.mapper.LocationMapper;
import ru.practicum.requestParams.AdminGetEventsParam;
import ru.practicum.requestParams.GetEventsParam;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.eventsDto.UserStateAction.CANCEL_REVIEW;
import static ru.practicum.eventsDto.UserStateAction.SEND_TO_REVIEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final HitServerHttpClientImpl serverHttpClient;

    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {


        List<EventWithConfirmed> events = eventsRepository.findAllByIdPageable(userId, PageRequest.of(from, size));

        LocalDateTime earliestStart = events.stream().filter(e -> e.getEvent().getState() == State.PUBLISHED)
                .map(e -> e.getEvent().getPublishedOn())
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(100));

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getEvent().getId())
                .collect(Collectors.toList());

        List<HitResponseDto> stats = serverHttpClient.getStats(earliestStart, LocalDateTime.now(), uris, true);
        Map<String, Long> viewsMap = stats.stream()
                .collect(Collectors.toMap(HitResponseDto::getUri, HitResponseDto::getHits));


        return events.stream()
                .map(row -> {
                    EventShortDto dto = EventMapper.mapToEventShortDto(row.getEvent());
                    dto.setConfirmedRequests(row.getConfirmedRequests());
                    dto.setViews(viewsMap.getOrDefault("/events/" + row.getEvent().getId(), 0L));
                    return dto;
                })
                .collect(Collectors.toList());

    }

    public EventWithOutPublishedOnDto createEvent(Long userId, NewEventDto eventDto) {
        Event event = EventMapper.mapToEvent(eventDto);
        event.setCategory(getCategoryById(eventDto.getCategory()));
        event.setInitiator(getUserById(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        if (eventDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (eventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (eventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        try {
            return EventMapper.mapToEventWithOutPubDto(eventsRepository.save(event));
        } catch (DataIntegrityViolationException e) {
            throw new BadTimeException("The event starts no earlier than 2 hours after publication");
        }
    }

    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event event = getEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new AccessException("You have no access for this operation.");
        }

        return EventMapper.mapToFullEventDto(event, getViews(event), requestsRepository.getConfirmedRequestsCount(eventId));
    }

    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto request) {
        Event event = getEventById(eventId);

        return updateEvent(event, request, request.getStateAction());
    }

    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto request) {
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new AccessException("You have no access for this operation.");
        }
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("You can't update this event");
        }
        if (request.getParticipantLimit() != null && request.getParticipantLimit() < 0) {
            throw new BadRequestException("Participant limit must be positive or zero");
        }

        return updateEvent(event, request, request.getStateAction());
    }

    public List<EventFullDto> getAllEventsAdmin(AdminGetEventsParam param) {

        List<EventWithConfirmed> events = new ArrayList<>();
        if (param.getUsers() == null) {
            param.setUsers(new ArrayList<>());
        }
        if (param.getStates() == null) {
            param.setStates(new ArrayList<>());
        }
        if (param.getCategories() == null) {
            param.setCategories(new ArrayList<>());
        }
        if (param.getRangeStart() == null && param.getRangeEnd() != null) {
            events = eventsRepository.findEventsWithEndDate(param.getUsers(), param.getStates(), param.getCategories(), param.getRangeEnd(), PageRequest.of(param.getFrom(), param.getSize()));
        }

        if (param.getRangeEnd() == null && param.getRangeStart() != null) {
            events = eventsRepository.findEventsWithStartDate(param.getUsers(), param.getStates(), param.getCategories(), param.getRangeStart(), PageRequest.of(param.getFrom(), param.getSize()));
        }

        if (param.getRangeStart() == null && param.getRangeEnd() == null) {
            events = eventsRepository.findEventsWithoutDates(param.getUsers(), param.getStates(), param.getCategories(), PageRequest.of(param.getFrom(), param.getSize()));
        }
        if (param.getRangeStart() != null && param.getRangeEnd() != null) {
            events = eventsRepository.findAllEvents(param.getUsers(), param.getStates(), param.getCategories(), param.getRangeStart(), param.getRangeEnd(), PageRequest.of(param.getFrom(), param.getSize()));
        }

        if (param.getUsers().isEmpty() && param.getStates().isEmpty() && param.getCategories().isEmpty() && param.getRangeStart() == null && param.getRangeEnd() == null) {
            events = eventsRepository.findAllEvents(PageRequest.of(param.getFrom(), param.getSize()));
        }

        return events.stream().map(row -> {

            EventFullDto dto = EventMapper.mapToFullEventDto(row.getEvent(), getViews(row.getEvent()), row.getConfirmedRequests());
            return dto;
        }).collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = getEventById(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Event with ID = " + eventId + ", not found.");
        }
        serverHttpClient.saveHit("/events/" + eventId, request.getRemoteAddr(), LocalDateTime.now());
        return EventMapper.mapToFullEventDto(event, getViews(event), requestsRepository.getConfirmedRequestsCount(eventId));
    }

    public List<EventShortDto> getEvents(GetEventsParam param,
                                         HttpServletRequest request
    ) {
        if (param.getRangeStart() == null && param.getRangeEnd() == null) {
            param.setRangeStart(LocalDateTime.now());
        }

        if (param.getRangeEnd() != null && param.getRangeEnd().isBefore(param.getRangeStart())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        if (param.getCategories() == null) {
            param.setCategories(new ArrayList<>());
        }

        List<EventWithConfirmed> events = eventsRepository.findEventsPublic(param.getText(),
                param.getCategories(), param.getPaid(), param.getRangeStart(), param.getRangeEnd(), param.getOnlyAvailable(), PageRequest.of(param.getFrom(), param.getSize()));


        LocalDateTime earliestStart = events.stream().filter(e -> e.getEvent().getState() == State.PUBLISHED)
                .map(e -> e.getEvent().getPublishedOn())
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(100));

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getEvent().getId())
                .collect(Collectors.toList());


        List<HitResponseDto> stats = serverHttpClient.getStats(earliestStart, LocalDateTime.now(), uris, true);
        Map<String, Long> viewsMap = stats.stream()
                .collect(Collectors.toMap(HitResponseDto::getUri, HitResponseDto::getHits));


        List<EventShortDto> shortDtoEvents = events.stream()
                .map(row -> {
                    EventShortDto dto = EventMapper.mapToEventShortDto(row.getEvent());
                    dto.setConfirmedRequests(row.getConfirmedRequests());
                    dto.setViews(viewsMap.getOrDefault("/events/" + row.getEvent().getId(), 0L));
                    return dto;
                })
                .collect(Collectors.toList());

        serverHttpClient.saveHit("/events", request.getRemoteAddr(), LocalDateTime.now());

        if (param.getSort() == null) {
            return shortDtoEvents;
        }
        return switch (param.getSort()) {
            case "EVENT_DATE" ->
                    shortDtoEvents.stream().sorted(Comparator.comparing(EventShortDto::getEventDate).reversed()).collect(Collectors.toList());
            case "VIEWS" ->
                    shortDtoEvents.stream().sorted(Comparator.comparing(EventShortDto::getViews).reversed()).collect(Collectors.toList());
            default -> throw new BadRequestException("Incorrectly made request.");
        };
    }

    private EventFullDto updateEvent(Event event, BaseUpdateEventRequest request, StateAction stateAction) {
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            event.setCategory(categoriesRepository
                    .findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with ID = " + request.getCategory() + ", not found.")));
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadTimeException("Event date must be at least 2 hours from now");
            }
            if (stateAction != CANCEL_REVIEW) {
                event.setEventDate(request.getEventDate());
            }
        }
        if (request.getLocation() != null) {
            event.setLocation(LocationMapper.mapToLocation(request.getLocation()));
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (stateAction instanceof UserStateAction) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);

                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + stateAction);
            }
        }
        if (stateAction instanceof AdminStateAction) {
            switch (stateAction) {
                case AdminStateAction.PUBLISH_EVENT:
                    if (event.getState() != State.PENDING) {
                        throw new ConflictException("You cant publish this event");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case AdminStateAction.REJECT_EVENT:
                    if (event.getState() == State.PUBLISHED) {
                        throw new ConflictException("You cant reject this event");
                    }
                    event.setState(State.CANCELED);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + stateAction);
            }
        }

        return EventMapper.mapToFullEventDto(eventsRepository.save(event), getViews(event), requestsRepository.getConfirmedRequestsCount(event.getId()));

    }

    private Category getCategoryById(Long categoryId) {
        return categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID = " + categoryId + ", not found."));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + ", not found."));
    }

    private Event getEventById(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID = " + eventId + ", not found."));
    }

    private Long getViews(Event event) {
        return serverHttpClient
                .getStats(event.getCreatedOn(), LocalDateTime.now(), List.of("/events/" + event.getId()), true)
                .stream().findFirst().map(HitResponseDto::getHits).orElse(0L);
    }

}
