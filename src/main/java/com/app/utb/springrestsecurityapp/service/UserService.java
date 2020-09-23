package com.app.utb.springrestsecurityapp.service;

import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.ui.response.UserRest;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto getUser(String email);
}
