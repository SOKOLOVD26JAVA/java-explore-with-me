package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.AccessException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.users.mapper.UsersMapper;
import ru.practicum.users.model.User;
import ru.practicum.usersDto.NewUserDto;
import ru.practicum.usersDto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UsersService {
    private final UserRepository userRepository;

    public UserDto createUser(Long adminId, NewUserDto newUserDto) {

        adminCheck(adminId);
        try {

            return UsersMapper.mapToUserDto(userRepository.save(UsersMapper.mapToUser(newUserDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Email already exists.");
        }
    }

    public List<UserDto> findByIds(Long adminId, List<Long> ids) {

        adminCheck(adminId);

        return userRepository.findAllById(ids).stream().map(UsersMapper::mapToUserDto).collect(Collectors.toList());
    }

    public List<UserDto> findAll(Long adminId, int from, int size) {

        adminCheck(adminId);

        return userRepository.findAll(PageRequest.of(from, size))
                .stream().map(UsersMapper::mapToUserDto).collect(Collectors.toList());
    }

    public void deleteUserById(Long adminId, Long userId) {

        adminCheck(adminId);

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with ID = " + userId + ", not found."));

        userRepository.deleteById(userId);

    }

    private void adminCheck(Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin with ID = " + adminId + ", not found."));

        if (!admin.getIsAdmin()) {
            throw new AccessException("You have no access for this operation.");
        }
    }


}
