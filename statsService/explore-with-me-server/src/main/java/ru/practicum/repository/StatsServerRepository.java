package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.hitDto.HitResponseDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerRepository extends JpaRepository<Hit, Long> {

    final String DTO_IMPORT = "ru.practicum.hitDto.HitResponseDto";

    @Query("SELECT new " + DTO_IMPORT + "(h.app,h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT(h.ip)) DESC")

    List<HitResponseDto> getWithUriUniqueStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                               @Param("uris") List<String> uris);

    @Query("SELECT new " + DTO_IMPORT + "(h.app,h.uri, COUNT(h)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h) DESC")
    List<HitResponseDto> getWithUriAllStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                            @Param("uris") List<String> uris);
}
