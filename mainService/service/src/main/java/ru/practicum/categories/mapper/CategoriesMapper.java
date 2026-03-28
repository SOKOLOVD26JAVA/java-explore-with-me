package ru.practicum.categories.mapper;

import ru.practicum.categories.model.Category;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.categoryDto.NewCategoryDto;

public class CategoriesMapper {

    public static Category mapToCategory(NewCategoryDto dto) {
        Category category = new Category();

        category.setName(dto.getName());

        return category;
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();

        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }


}
