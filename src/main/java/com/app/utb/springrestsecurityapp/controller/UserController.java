package com.app.utb.springrestsecurityapp.controller;

import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.ui.request.UserDetailsRequestModel;
import com.app.utb.springrestsecurityapp.ui.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

import static java.awt.PageAttributes.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){
        UserRest returnedValue = new UserRest();
        UserDto userDto = userService.getUserById(id);

        BeanUtils.copyProperties(userDto, returnedValue);
        return returnedValue;
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
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
