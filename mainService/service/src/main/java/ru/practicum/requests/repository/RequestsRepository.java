package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.requests.model.Request;
import ru.practicum.requestsDto.EventRequestStatus;

import java.util.List;


public interface RequestsRepository extends JpaRepository<Request, Long> {

    @Query("SELECT COUNT(r) " +
            "FROM Request r " +
            "WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Long getConfirmedRequestsCount(@Param("eventId") Long eventId);

    List<Request> findAllByEventId(Long eventId);

    @Modifying
    @Query("UPDATE Request r SET r.status = :status " +
            "WHERE r.event.id = :eventId AND r.status = 'PENDING'")
    void updateRequestStatus(@Param("eventId") Long eventId,
                             @Param("status") EventRequestStatus status);

    List<Request> findAllByRequesterId(Long requesterId);


}
