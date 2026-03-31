package ru.practicum.commentDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank(message = "Field: text. Error: max length must be 2000.")
    private String text;
}
