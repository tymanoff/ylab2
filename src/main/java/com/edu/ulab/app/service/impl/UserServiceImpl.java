package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.impl.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Got user create request: {}", userDto);

        User user = userMapper.userDtoToUser(userDto);
        log.info("Mapped userDto: {}", userDto);

        User saveUser = userStorage.save(user);
        log.info("Save user: {}", userDto);

        return userMapper.userToUserDto(saveUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Got user update request: {}", userDto);

        User user = userMapper.userDtoToUser(userDto);
        log.info("Mapped userDto: {}", userDto);

        User updateUser = userStorage.update(user);
        log.info("Update user: {}", userDto);

        return userMapper.userToUserDto(updateUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Got user get user with id: {}", id);

        User user;
        log.info("Check user in database which id: {}", id);
        if (userStorage.existsById(id)) {
            user = userStorage.findById(id);
            log.info("Get user: {}", user);
        } else {
            throw new NotFoundException("No user with id: " + id);
        }

        return userMapper.userToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Got user delete user with id : {}", id);

        log.info("Check user in database which id: {}", id);
        if (userStorage.existsById(id)) {
            userStorage.deleteById(id);
            log.info("Delete user with id: {}", id);
        } else {
            throw new NotFoundException("No user with id: " + id);
        }
    }
}
