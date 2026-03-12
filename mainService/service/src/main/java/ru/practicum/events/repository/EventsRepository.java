package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventWithConfirmed;
import ru.practicum.eventsDto.State;


import java.time.LocalDateTime;
import java.util.List;


public interface EventsRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e as event, COUNT(r) as confirmedRequests FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE e.initiator.id = :userId " +
            "GROUP BY e")
    List<EventWithConfirmed> findAllByIdPageable(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT e as event, COUNT(r) as confirmedRequests " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) AND " +
            "(:states IS NULL OR e.state IN :states) AND " +
            "(:categories IS NULL OR e.category.id IN :categories) AND " +
            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd) " +
            "GROUP BY e")
    List<EventWithConfirmed> findAllEvents(@Param("users") List<Long> ids,
                                           @Param("states") List<State> states,
                                           @Param("categories") List<Long> categories,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                           Pageable pageable);

    @Query("SELECT e as event, COUNT(r) as confirmedRequests " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE (:text IS NULL OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))) AND " +  // ← добавил ))
            "(:catIds IS NULL OR e.category.id IN :catIds) AND " +
            "(:paid IS NULL OR e.paid = :paid) AND " +
            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd) " +
            "GROUP BY e " +
            "HAVING (:onlyAvailable IS NULL OR e.participantLimit > COUNT(r) OR e.participantLimit = 0)")
    List<EventWithConfirmed> findEventsPublic(@Param("text") String text,
                                              @Param("catIds") List<Long> catIds,
                                              @Param("paid") Boolean paid,
                                              @Param("rangeStart") LocalDateTime rangeStart,
                                              @Param("rangeEnd") LocalDateTime rageEnd,
                                              @Param("onlyAvailable") Boolean onlyAvailable,
                                              Pageable pageable);
}


