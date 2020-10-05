package com.app.utb.springrestsecurityapp.controller;

import com.app.utb.springrestsecurityapp.dto.AddressDto;
import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.service.impl.UserServiceImpl;
import com.app.utb.springrestsecurityapp.ui.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    UserServiceImpl userService;
    @InjectMocks
    UserController userController;

    UserDto userDto;

    final String USER_ID = "hsbdjhsc554";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Sergey");
        userDto.setLastName("Surname");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationToken("ksdjskd");
        userDto.setEmailVerificationStatus(false);
        userDto.setAddresses(getAddressDtos());
        userDto.setEncryptedPassword("sdsjdsjd");
        userDto.setUserId(USER_ID);
    }

    private List<AddressDto> getAddressDtos(){

        AddressDto shippingAddressDto = new AddressDto();
        shippingAddressDto.setType("Shipping");
        shippingAddressDto.setAddressId("Vancover");
        shippingAddressDto.setCountry("Canada");
        shippingAddressDto.setPostalCode("ABC123");
        shippingAddressDto.setStreetName("123 Street name");


        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setType("Billing");
        billingAddressDto.setAddressId("Vancover");
        billingAddressDto.setCountry("Canada");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");

        return Arrays.asList(shippingAddressDto, billingAddressDto);
    }

    @Test
    void getUser() {

        when(userService.getUserById(anyString())).thenReturn(userDto);
        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertNotNull(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
    }
}