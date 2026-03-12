package ru.practicum.compilationsDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {

    private List<Long> events;
    @NotNull(message = "Field: pinned. Error: must not be blank. Value: null")
    private Boolean pinned;
    @NotBlank(message = "Field: title. Error: must not be blank. Value: null")
    private String title;
}
