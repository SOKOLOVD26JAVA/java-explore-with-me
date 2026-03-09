package ru.practicum.events.mapper;


import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.events.model.Event;
import ru.practicum.eventsDto.EventFullDto;
import ru.practicum.eventsDto.EventShortDto;
import ru.practicum.eventsDto.EventWithOutPublishedOnDto;
import ru.practicum.eventsDto.NewEventDto;
import ru.practicum.locationModel.mapper.LocationMapper;
import ru.practicum.users.mapper.UsersMapper;

public class EventMapper {

    public static Event mapToEvent(NewEventDto dto) {
        Event event = new Event();

        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(LocationMapper.mapToLocation(dto.getLocation()));
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());

        return event;
    }

    public static EventWithOutPublishedOnDto mapToEventWithOutPubDto(Event event) {
        EventWithOutPublishedOnDto dto = new EventWithOutPublishedOnDto();

        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setLocation(LocationMapper.mapToLocationDto(event.getLocation()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setTitle(event.getTitle());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setState(event.getState());
        dto.setInitiator(UsersMapper.mapToUserShortDto(event.getInitiator()));
        dto.setCategory(CategoriesMapper.mapToCategoryDto(event.getCategory()));
        dto.setViews(0L);
        dto.setConfirmedRequest(0L);

        return dto;
    }

    public static EventFullDto mapToFullEventDto(Event event, Long views, Long confirmedRequests) {

        EventFullDto dto = new EventFullDto();

        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setLocation(LocationMapper.mapToLocationDto(event.getLocation()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setTitle(event.getTitle());
        dto.setState(event.getState());
        dto.setInitiator(UsersMapper.mapToUserShortDto(event.getInitiator()));
        dto.setCategory(CategoriesMapper.mapToCategoryDto(event.getCategory()));
        dto.setViews(views);
        dto.setConfirmedRequest(confirmedRequests);

        return dto;
    }

    public static EventShortDto mapToEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();

        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoriesMapper.mapToCategoryDto(event.getCategory()));
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UsersMapper.mapToUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());

        return eventShortDto;
    }
}
