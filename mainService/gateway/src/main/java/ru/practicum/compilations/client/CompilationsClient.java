package ru.practicum.compilations.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.compilationsDto.CompilationDto;
import ru.practicum.compilationsDto.NewCompilationDto;
import ru.practicum.exceptions.GatewayException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationsClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public CompilationDto createCompilation(NewCompilationDto dto) {
        String url = createUrl("/admin/compilations");
        HttpEntity<NewCompilationDto> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<CompilationDto> response = restTemplate
                    .exchange(url, HttpMethod.POST, request, CompilationDto.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public void deleteCompilation(Long compId) {
        String url = createUrl("/admin/compilations/" + compId);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public CompilationDto updateCompilation(Long compId,
                                            NewCompilationDto dto) {
        String url = createUrl("/admin/compilations/" + compId);
        HttpEntity<NewCompilationDto> request = new HttpEntity<>(dto);
        try {
            ResponseEntity<CompilationDto> response = restTemplate
                    .exchange(url, HttpMethod.PATCH, request, CompilationDto.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<CompilationDto> getAllCompilations(Boolean pinned,
                                                   int from,
                                                   int size) {
        String url = createUrl("/compilations?pinned=" + pinned + "&from=" + from + "&size=" + size);

        try {
            ResponseEntity<List<CompilationDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CompilationDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public CompilationDto getCompilationById(Long compId) {
        String url = createUrl("/compilations/" + compId);

        try {
            ResponseEntity<CompilationDto> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, CompilationDto.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
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
