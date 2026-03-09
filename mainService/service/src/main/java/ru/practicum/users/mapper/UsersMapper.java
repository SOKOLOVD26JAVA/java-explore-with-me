package ru.practicum.users.mapper;

import ru.practicum.users.model.User;
import ru.practicum.usersDto.NewUserDto;
import ru.practicum.usersDto.UserDto;
import ru.practicum.usersDto.UserShortDto;

public class UsersMapper {

    public static User mapToUser(NewUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public static UserShortDto mapToUserShortDto(User user) {
        UserShortDto dto = new UserShortDto();

        dto.setId(user.getId());
        dto.setName(user.getName());

        return dto;
    }
}
