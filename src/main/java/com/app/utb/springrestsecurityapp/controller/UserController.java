package com.app.utb.springrestsecurityapp.controller;

import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.exceptions.UserServiceException;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.ui.request.UserDetailsRequestModel;
import com.app.utb.springrestsecurityapp.ui.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel)
            throws Exception
    {

        if( userDetailsRequestModel.getFirstName().isEmpty())
            throw new UserServiceException("user object is null");

        //UserDto userDto=new UserDto();
        //BeanUtils.copyProperties(userDetailsRequestModel, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetailsRequestModel, UserDto.class);

        UserDto storedValue = userService.saveUser(userDto);

        UserRest returnedValue = new UserRest();

      //  BeanUtils.copyProperties(userDto, returnedValue);
        returnedValue = modelMapper.map(storedValue, UserRest.class);
        return returnedValue;
    }

    @PutMapping(path = "/{id}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public UserRest updateUser(
            @PathVariable String id,
            @RequestBody UserDetailsRequestModel userDetailsRequestModel
    ){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);

        UserRest returnedValue = new UserRest();
        BeanUtils.copyProperties(updatedUser, returnedValue);
        return returnedValue;
    }

    @DeleteMapping(path = "/{id}",
        produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    }
    )
    public OperationStatusModel deleteUser(
            @PathVariable String id
    ){
        OperationStatusModel returnedValue = new OperationStatusModel();
        returnedValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnedValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnedValue;
    }

    @GetMapping(
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public List<UserRest> getUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit
    ){
        List<UserRest> returnedValue = new ArrayList<>();
        List<UserDto> userDtoList = userService.getUsers(page, limit);

        for (UserDto user : userDtoList) {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties( user, userRest);
            returnedValue.add(userRest);
        }

        return returnedValue;
    }
}
