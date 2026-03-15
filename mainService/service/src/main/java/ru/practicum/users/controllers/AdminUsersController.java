package ru.practicum.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.service.UsersService;
import ru.practicum.usersDto.NewUserDto;
import ru.practicum.usersDto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {

    private final UsersService usersService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto createUser(@RequestBody NewUserDto newUserDto) {

        return usersService.createUser(newUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        if (ids == null || ids.isEmpty()) {
            return usersService.findAll(from, size);
        } else {
            return usersService.findByIds(ids);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {

        usersService.deleteUserById(userId);
    }
}
