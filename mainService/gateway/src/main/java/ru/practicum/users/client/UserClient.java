package ru.practicum.users.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.exceptions.GatewayException;
import ru.practicum.usersDto.NewUserDto;
import ru.practicum.usersDto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public UserDto createUser(NewUserDto newUserDto) {
        String uri = createUrl("/admin/users");
        HttpEntity<NewUserDto> request = new HttpEntity<>(newUserDto);
        try {
            ResponseEntity<UserDto> response = restTemplate
                    .exchange(uri, HttpMethod.POST, request, UserDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public void deleteUser(Long userId) {
        String url = createUrl("/admin/users/" + userId);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<UserDto> getUsers(List<Long> ids,
                                  int from,
                                  int size) {
        String baseUri = "/admin/users";
        StringBuilder uri = new StringBuilder();
        uri.append(baseUri);
        if (ids != null && !ids.isEmpty()) {
            String strIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
            uri.append("?ids=").append(strIds);
        }
        if (uri.toString().contains("?")) {
            uri.append("&from=").append(from).append("&size=").append(size);
        } else {
            uri.append("?from=").append(from).append("&size=").append(size);
        }

        String fullUri = createUrl(uri.toString());

        try {
            ResponseEntity<List<UserDto>> response = restTemplate
                    .exchange(fullUri, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDto>>() {
                    });
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
