package com.app.utb.springrestsecurityapp.controller;

import com.app.utb.springrestsecurityapp.dto.AddressDto;
import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.exceptions.UserServiceException;
import com.app.utb.springrestsecurityapp.service.AddressService;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.ui.request.UserDetailsRequestModel;
import com.app.utb.springrestsecurityapp.ui.response.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.EntityMode;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderDsl;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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




    @ApiOperation(
            value = "The Get User Details Web Service Endpoint",
            notes = "${userController.GetUser.ApiOperation.Notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "authorization",
                    value = "${userController.authorizationHeader.description}",
                    paramType ="header"
            )
    })
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





    @ApiOperation(
            value = "The Create User  Web Service Endpoint",
            notes = "${userController.CreateUser.ApiOperation.Notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "authorization",
                    value = "${userController.authorizationHeader.description}",
                    paramType ="header"
            )
    })
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




    @ApiOperation(
            value = "The Update User Details Web Service Endpoint",
            notes = "${userController.UpdateUser.ApiOperation.Notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "authorization",
                    value = "${userController.authorizationHeader.description}",
                    paramType ="header"
            )
    })
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




    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    //@PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    //@Secured("ROLE_ADMIN   ")
    @ApiOperation(
            value = "The Delete User Details Web Service Endpoint",
            notes = "${userController.DeleteUser.ApiOperation.Notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "authorization",
                    value = "${userController.authorizationHeader.description}",
                    paramType ="header"
            )
    })
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



    @ApiOperation(
            value = "The Get Users  Web Service Endpoint",
            notes = "${userController.GetUsers.ApiOperation.Notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "authorization",
                    value = "${userController.authorizationHeader.description}",
                    paramType ="header"
            )
    })
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





    @ApiOperation(
            value = "The Get Addresses  Web Service Endpoint",
            notes = "${userController.GetAddresses.ApiOperation.Notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "authorization",
                    value = "${userController.authorizationHeader.description}",
                    paramType ="header"
            )
    })
    //http://localhost:2020/spring-rest-security-app/users/{userId}/addresses
    @GetMapping(
            path = "/{id}/addresses",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public CollectionModel<AddressRest> getAddresses(
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

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddresses(userId))
                .withSelfRel();

        for (AddressRest addressRest: returnedValue) {

            Link selfRel = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                    .getAddress(userId, addressRest.getAddressId()))
                    .withSelfRel();

            addressRest.add(selfRel );
        }


        return CollectionModel.of(returnedValue, userLink, selfLink);

    }




    @ApiOperation(
            value = "The Get Address Details Web Service Endpoint",
            notes = "${userController.GetAddress.ApiOperation.Notes}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "authorization",
                    value = "${userController.authorizationHeader.description}",
                    paramType ="header"
            )
    })
    @GetMapping(
            path = "/{userId}/addresses/{addressId}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public EntityModel<AddressRest> getAddress(
            @PathVariable("addressId") String addressId,
            @PathVariable("userId") String userId
    ){
        AddressDto addressDto = addressService.getAddressByAddressId(addressId);
        if(addressDto == null)
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        ModelMapper modelMapper = new ModelMapper();

        AddressRest returnedValue = modelMapper.map(addressDto, AddressRest.class);
        //http://localhost:8080/{userId}/addresses/addressId
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).withRel("user");
//                .slash(userId)
//                .withRel("user");
        Link userAddressLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddresses(userId))
                .withRel("addresses");
//                .slash(userId)
//                .slash("addresses")
//                .withRel("addresses");
        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddress(userId, addressId))
                .withSelfRel();
//                .slash(userId)
//                .slash("addresses")
//                .slash(addressId)
//                .withSelfRel();

        /*returnedValue.add(userLink);
        returnedValue.add(userAddressLink);
        returnedValue.add(selfLink);*/

        return   EntityModel.of(returnedValue, Arrays.asList(userLink, userAddressLink, selfLink));
    }
}
