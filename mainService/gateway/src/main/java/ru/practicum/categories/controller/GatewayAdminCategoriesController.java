package ru.practicum.categories.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public CategoryDto createCategory(@RequestBody NewCategoryDto categoryDto) {
        return client.createCategory(categoryDto);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategoryById(@PathVariable Long categoryId) {
        client.deleteCategoryById(categoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(@PathVariable Long categoryId, @PathVariable NewCategoryDto categoryDto) {
        return client.updateCategory(categoryId, categoryDto);
    }
}
