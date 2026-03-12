package ru.practicum.usersDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserDto {
    @Email(message = "Field: email. Error: incorrect email format.")
    private String email;
    @NotBlank(message = "Field: name. Error: must not be null. Value: null")
    private String name;
}
