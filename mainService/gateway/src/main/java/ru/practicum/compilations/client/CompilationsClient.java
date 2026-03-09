package ru.practicum.compilations.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.practicum.Headers;
import ru.practicum.compilationsDto.CompilationDto;
import ru.practicum.compilationsDto.NewCompilationDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationsClient {

    private final RestTemplate restTemplate;

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public CompilationDto createCompilation(Long adminId,
                                            NewCompilationDto dto) {

    }

    public void deleteCompilation(Long adminId,
                                  Long compId) {

    }

    public CompilationDto updateCompilation(Long adminId,
                                            Long compId,
                                            NewCompilationDto dto) {

    }

    public List<CompilationDto> getAllCompilations(Boolean pinned,
                                                   int from,
                                                   int size) {

    }

    public CompilationDto getCompilationById(Long compId) {

    }


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.ALL));

        return headers;
    }

    private HttpHeaders createHeadersWithUserId(Long userId) {
        HttpHeaders headers = createHeaders();
        headers.set("X-Sharer-User-Id", userId.toString());
        return headers;
    }

    private String createUrl(String path) {
        return serverUrl + path;
    }
}
