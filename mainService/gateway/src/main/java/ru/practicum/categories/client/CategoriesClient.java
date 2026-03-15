package ru.practicum.categories.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.categoryDto.NewCategoryDto;
import ru.practicum.exceptions.GatewayException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoriesClient {

    private final RestTemplate restTemplate;

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        String url = createUrl("/admin/categories");
        HttpEntity<NewCategoryDto> request = new HttpEntity<>(categoryDto);
        try {
            ResponseEntity<CategoryDto> response = restTemplate
                    .exchange(url, HttpMethod.POST, request, CategoryDto.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public void deleteCategoryById(Long catId) {
        String url = createUrl("/admin/categories/" + catId);;
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public CategoryDto updateCategory(Long catId, NewCategoryDto categoryDto) {
        String url = createUrl("/admin/categories/" + catId);
        HttpEntity<NewCategoryDto> request = new HttpEntity<>(categoryDto);
        try {
            ResponseEntity<CategoryDto> response = restTemplate
                    .exchange(url, HttpMethod.PATCH, request, CategoryDto.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public List<CategoryDto> getAllCategories(int from, int size) {
        String url = createUrl("/categories?from=" + from + "&size=" + size);

        try {
            ResponseEntity<List<CategoryDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CategoryDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public CategoryDto getCategoryById(Long catId) {
        String url = createUrl("/categories/" + catId);
        try {
            ResponseEntity<CategoryDto> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, CategoryDto.class);

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
