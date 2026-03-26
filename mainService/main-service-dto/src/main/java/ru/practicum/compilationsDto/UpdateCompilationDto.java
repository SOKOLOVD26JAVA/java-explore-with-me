package ru.practicum.compilationsDto;


import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @Size(max = 50, message = "Field: title. Error: max length must be 50")
    private String title;
}
