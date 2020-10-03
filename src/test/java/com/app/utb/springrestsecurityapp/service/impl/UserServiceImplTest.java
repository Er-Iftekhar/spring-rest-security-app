package com.app.utb.springrestsecurityapp.service.impl;

import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userServiceImpl;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Sergey");
        userEntity.setLastName("jshdjshd");
        userEntity.setEncryptedPassword("sbsbcjbsj");

        Mockito.when(userRepository.findByEmail( ArgumentMatchers.anyString() )).thenReturn(userEntity);

    }
}