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
    public CategoryDto createCategory(@RequestBody NewCategoryDto categoryDto) {
        return categoriesService.createCategory(categoryDto);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategoryById(@PathVariable Long categoryId) {
        categoriesService.deleteCategoryById(categoryId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(@PathVariable Long categoryId, @RequestBody NewCategoryDto categoryDto) {
        return categoriesService.updateCategory(categoryId, categoryDto);
    }


}
