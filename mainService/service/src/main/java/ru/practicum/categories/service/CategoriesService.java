package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.categoryDto.CategoryDto;
import ru.practicum.categoryDto.NewCategoryDto;
import ru.practicum.exceptions.AccessException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;

    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        try {
            return CategoriesMapper.mapToCategoryDto(categoriesRepository.save(CategoriesMapper.mapToCategory(categoryDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category name already exists");
        }
    }

    public void deleteCategoryById(Long categoryId) {

        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID = " + categoryId + ", not found."));

        try {
            categoriesRepository.deleteById(category.getId());
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("The category is not empty");
        }
    }

    public CategoryDto updateCategory(Long categoryId, NewCategoryDto categoryDto) {

        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with ID = " + categoryId + ", not found."));

        category.setName(categoryDto.getName());
        try {
            return CategoriesMapper.mapToCategoryDto(categoriesRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category name already exists");
        }
    }

    public List<CategoryDto> getAllCategories(int from, int size) {
        return categoriesRepository.findAll(PageRequest.of(from, size)).stream().map(CategoriesMapper::mapToCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long categoryId) {
        return CategoriesMapper.mapToCategoryDto(categoriesRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category with ID = " + categoryId + ", not found.")));
    }

    private void adminCheck(Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin with ID = " + adminId + ", not found."));

        if (!admin.getIsAdmin()) {
            throw new AccessException("You have no access for this operation.");
        }
    }


}
