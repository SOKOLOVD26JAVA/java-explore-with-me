package ru.practicum.events.model;

public interface EventWithConfirmed {
    Event getEvent();
    Long getConfirmedRequests();
}
