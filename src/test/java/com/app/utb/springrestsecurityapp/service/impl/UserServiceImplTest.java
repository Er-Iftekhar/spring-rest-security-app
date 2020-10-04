package com.app.utb.springrestsecurityapp.service.impl;

import com.app.utb.springrestsecurityapp.dto.AddressDto;
import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.entity.AddressEntity;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.exceptions.UserServiceException;
import com.app.utb.springrestsecurityapp.repositories.UserRepository;
import com.app.utb.springrestsecurityapp.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userServiceImpl;
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    Utils utils;

    String userId = "jshdjshd";
    String encryptedPassword = "sbsbcjbsj";

    UserEntity userEntity ;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setFirstName("Sergey");
        userEntity.setLastName("Monkey");
        userEntity.setEmail("test@test.com");
        userEntity.setId(1L);
        userEntity.setEmailVerificationToken("kjdhfjkd");
        userEntity.setEmailVerificationStatus(false);
        userEntity.setAddresses(getAddressEntity());

    }

    @Test
    void getUser() {
        when(userRepository.findByEmail( anyString() )).thenReturn(userEntity);
        UserDto userDto = userServiceImpl.getUser("dshdkjsh");
        assertNotNull(userDto);
        assertEquals("Sergey", userDto.getFirstName());

    }

    @Test
    void testGetUser_UsernameNotFoundException(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(
                UsernameNotFoundException.class,
                ()->{
                    userServiceImpl.getUser("sdjshkhdkjs");
                }
        );
    }

    @Test
    void testSaveUser(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(utils.generateAddressId(anyInt())).thenReturn("hdbhjsbdjh55");
        when(passwordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);




        UserDto userDto = new UserDto();
        userDto.setFirstName("Sergey");
        userDto.setUserId(userId);
        userDto.setLastName("Baigan");
        userDto.setPassword("pass");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressDtos());
        userDto.setId(1L);






        UserDto storedUserDetails = userServiceImpl.saveUser(userDto);

        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());

        verify(utils,times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(passwordEncoder, times(1)).encode("pass");
        verify(userRepository, times(1)).save(any(UserEntity.class));


    }

    @Test
    final void testCreateUser_CreateUserServiceException(){


        UserDto userDto = new UserDto();
        userDto.setFirstName("Sergey");
        userDto.setUserId(userId);
        userDto.setLastName("Baigan");
        userDto.setPassword("pass");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressDtos());
        userDto.setId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        assertThrows(UserServiceException.class,
                () ->{
            userServiceImpl.saveUser(userDto);
                }
                );
    }




    private  List<AddressDto> getAddressDtos(){

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

    private  List<AddressEntity> getAddressEntity(){

        List<AddressDto> addresses = getAddressDtos();
        Type listType = new TypeToken<List<AddressEntity>>(){}.getType();
        return  new ModelMapper().map(addresses, listType);
    }


}