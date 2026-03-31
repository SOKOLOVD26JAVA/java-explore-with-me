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
            "WHERE e.initiator.id IN :users AND " +
            "e.state IN :states AND " +
            "e.category.id IN :categories AND " +
            "e.eventDate >= CAST(:rangeStart AS timestamp) AND " +
            "e.eventDate <= CAST(:rangeEnd AS timestamp) " +
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
            "WHERE (:text IS NULL OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', CAST(:text AS text), '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', CAST(:text AS text), '%')))) AND " +
            "(:catIds IS NULL OR e.category.id IN :catIds) AND " +
            "(:paid IS NULL OR e.paid = :paid) AND " +
            "(CAST(:rangeStart AS date) IS NULL OR e.eventDate >= :rangeStart) AND " +
            "(CAST(:rangeEnd AS date) IS NULL OR e.eventDate <= :rangeEnd) " +
            "GROUP BY e " +
            "HAVING (CAST(:onlyAvailable AS boolean) IS NULL OR e.participantLimit > COUNT(r) OR e.participantLimit = 0)")
    List<EventWithConfirmed> findEventsPublic(@Param("text") String text,
                                              @Param("catIds") List<Long> catIds,
                                              @Param("paid") Boolean paid,
                                              @Param("rangeStart") LocalDateTime rangeStart,
                                              @Param("rangeEnd") LocalDateTime rangeEnd,
                                              @Param("onlyAvailable") Boolean onlyAvailable,
                                              Pageable pageable);

    @Query("SELECT e as event, COUNT(r) as confirmedRequests " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "GROUP BY e")
    List<EventWithConfirmed> findEventsWithoutDates(@Param("users") List<Long> users,
                                                    @Param("states") List<State> states,
                                                    @Param("categories") List<Long> categories,
                                                    Pageable pageable);

    @Query("SELECT e as event, COUNT(r) as confirmedRequests " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate >= :rangeStart " +
            "GROUP BY e")
    List<EventWithConfirmed> findEventsWithStartDate(@Param("users") List<Long> users,
                                                     @Param("states") List<State> states,
                                                     @Param("categories") List<Long> categories,
                                                     @Param("rangeStart") LocalDateTime rangeStart,
                                                     Pageable pageable);


    @Query("SELECT e as event, COUNT(r) as confirmedRequests " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate <= :rangeEnd " +
            "GROUP BY e")
    List<EventWithConfirmed> findEventsWithEndDate(@Param("users") List<Long> users,
                                                   @Param("states") List<State> states,
                                                   @Param("categories") List<Long> categories,
                                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                                   Pageable pageable);

    @Query("SELECT e as event, COUNT(r) as confirmedRequests " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "GROUP BY e")
    List<EventWithConfirmed> findAllEvents(Pageable pageable);

}


