package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.hitDto.HitDto;
import ru.practicum.hitDto.HitResponseDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServerService {

    private final StatsServerRepository repository;

    public HitDto saveHit(HitDto hitDto) {
        return HitMapper.mapToHitDto(repository.save(HitMapper.mapToHit(hitDto)));
    }

    public List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return repository.getWithOutUriUniqueStats(start, end);
            } else {
                return repository.getWithOutUriAllStats(start, end);
            }
        } else {
            if (unique) {
                return repository.getWithUriUniqueStats(start, end, uris);
            } else {
                return repository.getWithUriAllStats(start, end, uris);
            }
        }
    }

}
