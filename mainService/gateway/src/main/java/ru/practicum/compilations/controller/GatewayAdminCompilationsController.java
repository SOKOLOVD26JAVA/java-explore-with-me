package ru.practicum.compilations.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
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
    public CompilationDto createCompilation(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                            @Valid @RequestBody NewCompilationDto dto) {
        return client.createCompilation(adminId, dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                  @PathVariable Long compId) {
        client.deleteCompilation(adminId, compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto updateCompilation(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                            @Valid @RequestBody NewCompilationDto dto,
                                            @PathVariable Long compId) {
        return client.updateCompilation(adminId, compId, dto);
    }
}
