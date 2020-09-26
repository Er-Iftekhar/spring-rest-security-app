package com.app.utb.springrestsecurityapp.service;

import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.ui.response.UserRest;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto getUser(String email);

    UserDto getUserById(String userId);

    UserDto updateUser(String userId, UserDto user);

    void deleteUser(String userId);

    List<UserDto> getUsers(int page, int limit);
}
