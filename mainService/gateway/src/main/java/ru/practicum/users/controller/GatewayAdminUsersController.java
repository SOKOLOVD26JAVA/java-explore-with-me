package ru.practicum.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.users.client.UserClient;
import ru.practicum.usersDto.NewUserDto;
import ru.practicum.usersDto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class GatewayAdminUsersController {

    private final UserClient client;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto createUser(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                              @Valid @RequestBody NewUserDto newUserDto) {

        return client.createUser(adminId, newUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> getUsers(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId,
                                  @RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return client.getUsers(adminId, ids, from, size);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long adminId, @PathVariable Long userId) {

        client.deleteUser(adminId, userId);
    }
}
