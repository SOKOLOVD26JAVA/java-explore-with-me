package ru.practicum.compilations.mapper;

import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilationsDto.CompilationDto;
import ru.practicum.compilationsDto.NewCompilationDto;
import ru.practicum.events.mapper.EventMapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CompilationsMapper {

    public static Compilation mapToCompilation(NewCompilationDto dto) {
        Compilation compilation = new Compilation();

        compilation.setPinned(dto.getPinned());
        compilation.setTitle(dto.getTitle());

        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();

        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            dto.setEvents(compilation.getEvents().stream().map(EventMapper::mapToEventShortDto).collect(Collectors.toList()));
        } else {
            dto.setEvents(new ArrayList<>());
        }

        return dto;
    }
}
