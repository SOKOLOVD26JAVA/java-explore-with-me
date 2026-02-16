package ru.practicum.client;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.hitDto.HitDto;
import ru.practicum.hitDto.HitResponseDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HitServerHttpClientImpl implements HitServerHttpClient {

    private final RestTemplate restTemplate;

    @Value("${explore.server.url}")
    String serverUrl;

    public HitDto saveHit(String app, String uri, String ip, LocalDateTime timestamp) {
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
        String encodedStart = URLEncoder.encode(
                start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                StandardCharsets.UTF_8);

        StringBuilder uriBuilder = new StringBuilder(serverUrl + "/stats?");

        uriBuilder.append("start=").append(encodedStart).append("&end=").append(encodedEnd);

        if (uris != null && !uris.isEmpty()) {
            uriBuilder.append("&uris=").append(String.join(",", uris));
        }

        if (unique != null && unique) {
            uriBuilder.append("&unique=true");
        }

        String url = uriBuilder.toString();

        try {
            ResponseEntity<HitResponseDto[]> response = restTemplate.getForEntity(
                    url, HitResponseDto[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Ошибка получения хитов.");
        }
    }


}
