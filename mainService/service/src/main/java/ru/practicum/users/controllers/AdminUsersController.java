package ru.practicum.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
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
    public UserDto createUser(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                              @RequestBody NewUserDto newUserDto) {

        return usersService.createUser(adminId, newUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> getUsers(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                  @RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        if (ids == null && ids.isEmpty()) {
            return usersService.findAll(adminId, from, size);
        } else {
            return usersService.findByIds(adminId, ids);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId, @PathVariable Long userId) {

        usersService.deleteUserById(adminId, userId);
    }
}
