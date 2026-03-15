package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.eventsDto.UserStateAction.CANCEL_REVIEW;
import static ru.practicum.eventsDto.UserStateAction.SEND_TO_REVIEW;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final HitServerHttpClientImpl serverHttpClient;

    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {

        getUserById(userId);

        List<EventWithConfirmed> events = eventsRepository.findAllByIdPageable(userId, PageRequest.of(from, size));
        return events.stream().map(row -> {
            EventShortDto dto = EventMapper.mapToEventShortDto(row.getEvent());
            dto.setConfirmedRequests(row.getConfirmedRequests());
            dto.setViews(getViews(row.getEvent()));
            return dto;
        }).collect(Collectors.toList());

    }

    public EventWithOutPublishedOnDto createEvent(Long userId, NewEventDto eventDto) {
        Event event = EventMapper.mapToEvent(eventDto);
        event.setCategory(getCategoryById(eventDto.getCategory()));
        event.setInitiator(getUserById(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        try {
            return EventMapper.mapToEventWithOutPubDto(eventsRepository.save(event));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("The event starts no earlier than 2 hours after publication");
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

        if (request.getStateAction() == null) {
            throw new AccessException("You need to choose 'state action' parameter.");
        }
        return updateEvent(event, request, request.getStateAction());
    }

    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto request) {
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new AccessException("You have no access for this operation.");
        }
        if (event.getState() == State.PUBLISHED) {
            throw new AccessException("You can't update this event");
        }
        if (request.getStateAction() == null) {
            throw new AccessException("You need to choose 'state action' parameter.");
        }

        return updateEvent(event, request, request.getStateAction());
    }

    public List<EventFullDto> getAllEventsAdmin(List<Long> ids, List<State> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rageEnd, int from, int size) {


        List<EventWithConfirmed> events = eventsRepository.findAllEvents(ids, states, categories, rangeStart, rageEnd, PageRequest.of(from, size));

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
        serverHttpClient.saveHit("ewm-main-service", "/events/" + eventId, request.getRemoteAddr(), LocalDateTime.now());
        return EventMapper.mapToFullEventDto(event, getViews(event), requestsRepository.getConfirmedRequestsCount(eventId));
    }

    public List<EventShortDto> getEvents(String text,
                                         List<Long> catIds,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rageEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         int from,
                                         int size,
                                         HttpServletRequest request
    ) {
        if (rangeStart == null && rageEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        List<EventWithConfirmed> events = eventsRepository.findEventsPublic(text, catIds, paid, rangeStart, rageEnd, onlyAvailable, PageRequest.of(from, size));


        List<EventShortDto> shortDtoEvents = events.stream().map(row -> {
            EventShortDto dto = EventMapper.mapToEventShortDto(row.getEvent());
            dto.setConfirmedRequests(row.getConfirmedRequests());
            dto.setViews(getViews(row.getEvent()));
            return dto;
        }).toList();

        serverHttpClient.saveHit("ewm-main-service", "/events/", request.getRemoteAddr(), LocalDateTime.now());

        if (sort == null) {
            return shortDtoEvents;
        }
        return switch (sort) {
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
                throw new BadTimeException("Event date must be at least 2 hours from now");//Добавил проверку
            }
            event.setEventDate(request.getEventDate());
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
                        throw new AccessException("You cant publish this event");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case AdminStateAction.REJECT_EVENT:
                    if (event.getState() == State.PUBLISHED) {
                        throw new AccessException("You cant reject this event");
                    }
                    event.setState(State.REJECTED);
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

    private void adminCheck(Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin with ID = " + adminId + ", not found."));

        if (!admin.getIsAdmin()) {
            throw new AccessException("You have no access for this operation.");
        }
    }

}
