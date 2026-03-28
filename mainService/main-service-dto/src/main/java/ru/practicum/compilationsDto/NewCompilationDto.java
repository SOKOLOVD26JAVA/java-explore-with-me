package ru.practicum.compilationsDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @Size(max = 50, message = "Field: title. Error: max length must be 50")
    @NotBlank(message = "Field: title. Error: must not be blank. Value: null")
    private String title;
}
