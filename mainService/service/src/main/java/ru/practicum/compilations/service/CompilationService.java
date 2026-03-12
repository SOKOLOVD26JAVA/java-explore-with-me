package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.compilations.mapper.CompilationsMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.compilationsDto.CompilationDto;
import ru.practicum.compilationsDto.NewCompilationDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exceptions.AccessException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationsRepository compilationsRepository;
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;

    public CompilationDto createCompilation(Long adminId, NewCompilationDto dto) {

        adminCheck(adminId);

        Compilation compilation = CompilationsMapper.mapToCompilation(dto);
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            List<Event> events = eventsRepository.findAllById(dto.getEvents());
            compilation.setEvents(events);
        }

        compilationsRepository.save(compilation);
        return CompilationsMapper.mapToCompilationDto(compilation);
    }

    public void deleteCompilation(Long adminId, Long compId) {
        adminCheck(adminId);
        Compilation compilation = getCompilation(compId);

        compilationsRepository.delete(compilation);
    }

    public CompilationDto updateCompilation(Long adminId, Long compId, NewCompilationDto dto) {
        adminCheck(adminId);
        Compilation compilation = getCompilation(compId);

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }
        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            List<Event> events = eventsRepository.findAllById(dto.getEvents());
            compilation.setEvents(events);
        }

        return CompilationsMapper.mapToCompilationDto(compilationsRepository.save(compilation));
    }

    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        if (pinned != null) {
            return compilationsRepository.findByPinned(pinned, PageRequest.of(from, size))
                    .stream().map(CompilationsMapper::mapToCompilationDto).collect(Collectors.toList());
        } else {
            return compilationsRepository.findAll(PageRequest.of(from, size)).stream()
                    .map(CompilationsMapper::mapToCompilationDto).collect(Collectors.toList());
        }
    }

    public CompilationDto getCompilationById(Long compId) {
        return CompilationsMapper.mapToCompilationDto(getCompilation(compId));
    }

    private void adminCheck(Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin with ID = " + adminId + ", not found."));

        if (!admin.getIsAdmin()) {
            throw new AccessException("You have no access for this operation.");
        }
    }

    private Compilation getCompilation(Long compId) {
        return compilationsRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation with ID = " + compId + ", not found."));
    }

}
