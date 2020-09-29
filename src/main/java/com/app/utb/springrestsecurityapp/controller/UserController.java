package com.app.utb.springrestsecurityapp.controller;

import com.app.utb.springrestsecurityapp.dto.AddressDto;
import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.exceptions.UserServiceException;
import com.app.utb.springrestsecurityapp.service.AddressService;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.ui.request.UserDetailsRequestModel;
import com.app.utb.springrestsecurityapp.ui.response.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){

        UserDto userDto = userService.getUserById(id);

//        BeanUtils.copyProperties(userDto, returnedValue);
        ModelMapper modelMapper = new ModelMapper();
        UserRest returnedValue = modelMapper.map(userDto, UserRest.class);
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


    //http://localhost:2020/spring-rest-security-app/users/{userId}/addresses
    @GetMapping(
            path = "/{id}/addresses",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public List<AddressRest> getAddresses(
            @PathVariable("id") String userId
    ){
        ModelMapper modelMapper = new ModelMapper();
        List<AddressDto> addressDtoList = addressService.getAddresses(userId);
        List<AddressRest> returnedValue = new ArrayList<>();
        /*for (AddressDto addressDto:addressDtoList) {
            AddressRest addressRest = modelMapper.map(addressDto, AddressRest.class);
            returnedValue.add(addressRest);
        }*/

        Type listType = new TypeToken<List<AddressRest>>() {}.getType();
        returnedValue = modelMapper.map(addressDtoList, listType);
        return returnedValue;

    }

    @GetMapping(
            path = "/{userId}/addresses/{addressId}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public AddressRest getAddress(
            @PathVariable("addressId") String addressId
    ){
        AddressRest returnedValue = new AddressRest();
        AddressDto addressDto = addressService.getAddressByAddressId(addressId);
        if(addressDto == null)
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        ModelMapper modelMapper = new ModelMapper();

        returnedValue = modelMapper.map(addressDto, AddressRest.class);

        return  returnedValue;
    }
}
