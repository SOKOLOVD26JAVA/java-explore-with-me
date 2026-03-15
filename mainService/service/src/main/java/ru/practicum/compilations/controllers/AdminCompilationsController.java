package ru.practicum.compilations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.service.CompilationService;
import ru.practicum.compilationsDto.CompilationDto;
import ru.practicum.compilationsDto.NewCompilationDto;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody NewCompilationDto dto) {
        return compilationService.createCompilation(dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto updateCompilation(@RequestBody NewCompilationDto dto,
                                            @PathVariable Long compId) {
        return compilationService.updateCompilation(compId, dto);
    }
}
