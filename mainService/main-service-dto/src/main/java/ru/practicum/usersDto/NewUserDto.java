package ru.practicum.usersDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserDto {
    @Email(message = "Field: email. Error: incorrect email format.")
    @NotBlank(message = "Field: email. Error: must not be null. Value: null")
    @Size(min = 6, max = 254, message = "Field: email. Error: length must be between 2 and 250")
    private String email;
    @NotBlank(message = "Field: name. Error: must not be null. Value: null")
    @Size(min = 2, max = 250, message = "Field: name. Error: length must be between 3 and 120")
    private String name;
}
