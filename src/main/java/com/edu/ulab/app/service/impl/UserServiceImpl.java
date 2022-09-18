package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    // сгенерировать идентификатор
    // создать пользователя
    // вернуть сохраненного пользователя со всеми необходимыми полями id
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();

        user.setFullName(userDto.getFullName());
        user.setAge(userDto.getAge());
        user.setTitle(userDto.getTitle());

        return userMapper.userToUserDto(userStorage.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setFullName(userDto.getFullName());
        user.setAge(userDto.getAge());
        user.setTitle(userDto.getTitle());

        return userMapper.userToUserDto(userStorage.update(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.userToUserDto(userStorage.findById(id));
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteById(id);
    }
}
