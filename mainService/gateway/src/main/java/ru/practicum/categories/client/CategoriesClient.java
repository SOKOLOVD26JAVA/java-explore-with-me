package ru.practicum.categories.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.categoryDto.NewCategoryDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoriesClient {

    private final RestTemplate restTemplate;

    @Value("${explore.main.server.url}")
    private String serverUrl;

    public CategoryDto createCategory(Long adminId, NewCategoryDto categoryDto) {

    }

    public void deleteCategoryById(Long adminId, Long catId) {

    }

    public CategoryDto updateCategory(Long adminId, Long catId, NewCategoryDto categoryDto) {

    }

    public List<CategoryDto> getAllCategories(int from, int size) {

    }

    public CategoryDto getCategoryById(Long catId) {

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
