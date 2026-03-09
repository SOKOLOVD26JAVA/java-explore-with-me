package ru.practicum.categories.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.categoryDto.NewCategoryDto;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final CategoriesService categoriesService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                      @RequestBody NewCategoryDto categoryDto) {
        return categoriesService.createCategory(adminId, categoryDto);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategoryById(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                   @PathVariable Long categoryId) {
        categoriesService.deleteCategoryById(adminId, categoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                      @PathVariable Long categoryId, @PathVariable NewCategoryDto categoryDto) {
        return categoriesService.updateCategory(adminId, categoryId, categoryDto);
    }


}
