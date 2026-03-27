package ru.practicum.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.hitDto.HitDto;
import ru.practicum.hitDto.HitResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class HitServerHttpClientImpl implements HitServerHttpClient {

    private final RestTemplate restTemplate;

    private final DateTimeFormatter formatter;

    @Value("${explore.server.url}")
    private final String serverUrl;
    @Value("${explore.app.name}")
    private final String app;

    public HitServerHttpClientImpl(@Value("${explore.server.url}") String serverUrl, @Value("${explore.app.name}") String app) {
        this.restTemplate = new RestTemplate();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.serverUrl = serverUrl;
        this.app = app;
    }

    public HitDto saveHit(String uri, String ip, LocalDateTime timestamp) {
        HitDto hitDto = new HitDto();
        hitDto.setApp(app);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(timestamp);

        String url = serverUrl + "/hit";

        HttpEntity<HitDto> request = new HttpEntity<>(hitDto);
        try {
            ResponseEntity<HitDto> response = restTemplate.exchange(url, HttpMethod.POST, request, HitDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Ошибка при сохранении хита.");
        }


    }

    public List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serverUrl + "/stats")
                .queryParam("start", start.format(formatter))
                .queryParam("end", end.format(formatter));


        if (uris != null && !uris.isEmpty()) {
            uriBuilder.queryParam("uris", String.join(",", uris));
        }

        if (unique != null && unique) {
            uriBuilder.queryParam("unique", true);
        }

        String url = uriBuilder.build().toUriString();

        try {
            log.info(url);
            ResponseEntity<List<HitResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<HitResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Ошибка получения хитов.");
        }
    }


}
