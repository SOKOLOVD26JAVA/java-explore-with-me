package ru.practicum.categoryDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCategoryDto {
    @NotBlank(message = "Field: name. Error: must not be null. Value: null")
    @Size(max = 50, message = "Field: name. Error: max length must be 50")
    private String name;
}
