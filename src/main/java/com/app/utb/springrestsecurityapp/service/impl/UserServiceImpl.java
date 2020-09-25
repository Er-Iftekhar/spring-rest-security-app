package com.app.utb.springrestsecurityapp.service.impl;

import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.repositories.UserRepository;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.ui.response.ErrorMessages;
import com.app.utb.springrestsecurityapp.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder  passwordEncoder;

    private final Utils utils;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, Utils utils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserEntity enteredUser = userRepository.findByEmail(userDto.getEmail());
        if(enteredUser != null)
            throw new RuntimeException("create user already exists");

        UserEntity userEntity = new UserEntity();
        userDto.setUserId(utils.generateUserId(30));
        BeanUtils.copyProperties(userDto, userEntity);
//        userEntity.setUserId("testId");
        userEntity.setEncryptedPassword(passwordEncoder.encode("pass"));
        userEntity.setEmailVerificationToken("testToken");
        UserEntity storedUser = userRepository.save(userEntity);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(storedUser, returnedValue);

        return returnedValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity storedUser = userRepository.findByEmail(email);
        if(storedUser == null)
            throw  new UsernameNotFoundException("User "+email+ " not found");
        UserDto returnedValue = new UserDto();

        BeanUtils.copyProperties(storedUser, returnedValue);
        return returnedValue;
    }

    @Override
    public UserDto getUserById(String userId) {
        UserDto returnedValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity ==null)
            throw new UsernameNotFoundException("User with id="+userId+ " does not exists");
        BeanUtils.copyProperties(userEntity, returnedValue);
        return returnedValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserEntity userToBeUpdated = userRepository.findByUserId(userId);

        if( userToBeUpdated == null )
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());


        userToBeUpdated.setFirstName(user.getFirstName());
        userToBeUpdated.setLastName(user.getLastName());

//        BeanUtils.copyProperties(user, userToBeUpdated);
        UserEntity updatedUser = userRepository.save(userToBeUpdated);
        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(updatedUser, returnedValue);

        return returnedValue;
    }
}
