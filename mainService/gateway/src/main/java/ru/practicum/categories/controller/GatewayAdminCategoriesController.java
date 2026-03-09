package ru.practicum.categories.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.categories.client.CategoriesClient;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.categoryDto.NewCategoryDto;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class GatewayAdminCategoriesController {
    private final CategoriesClient client;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                      @RequestBody NewCategoryDto categoryDto) {
        return client.createCategory(adminId, categoryDto);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategoryById(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                   @PathVariable Long categoryId) {
        client.deleteCategoryById(adminId, categoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                      @PathVariable Long categoryId, @PathVariable NewCategoryDto categoryDto) {
        return client.updateCategory(adminId, categoryId, categoryDto);
    }
}
