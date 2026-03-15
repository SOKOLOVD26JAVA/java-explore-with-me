package ru.practicum.compilations.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.client.CompilationsClient;
import ru.practicum.compilationsDto.CompilationDto;
import ru.practicum.compilationsDto.NewCompilationDto;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class GatewayAdminCompilationsController {

    private final CompilationsClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto dto) {
        return client.createCompilation(dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        client.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto updateCompilation(@Valid @RequestBody NewCompilationDto dto,
                                            @PathVariable Long compId) {
        return client.updateCompilation(compId, dto);
    }
}
