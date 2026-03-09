package ru.practicum.compilations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
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
    public CompilationDto createCompilation(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                            @RequestBody NewCompilationDto dto) {
        return compilationService.createCompilation(adminId, dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                  @PathVariable Long compId) {
        compilationService.deleteCompilation(adminId, compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto updateCompilation(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                            @RequestBody NewCompilationDto dto,
                                            @PathVariable Long compId) {
        return compilationService.updateCompilation(adminId, compId, dto);
    }
}
