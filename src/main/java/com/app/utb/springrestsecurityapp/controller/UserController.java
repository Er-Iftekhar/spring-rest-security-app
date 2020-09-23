package com.app.utb.springrestsecurityapp.controller;

import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.ui.request.UserDetailsRequestModel;
import com.app.utb.springrestsecurityapp.ui.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser(){
        return "getUser() called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel){

        UserDto userDto=new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto storedValue = userService.saveUser(userDto);

        UserRest returnedValue = new UserRest();

        BeanUtils.copyProperties(userDto, returnedValue);
        return returnedValue;
    }

    @PutMapping
    public String updateUser(){
        return "updateUser() called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "deleteUser() called";
    }
}
