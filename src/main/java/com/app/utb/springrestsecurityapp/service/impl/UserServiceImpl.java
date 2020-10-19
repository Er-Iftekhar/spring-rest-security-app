package com.app.utb.springrestsecurityapp.service.impl;

import com.app.utb.springrestsecurityapp.dto.AddressDto;
import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.exceptions.UserServiceException;
import com.app.utb.springrestsecurityapp.repositories.UserRepository;
import com.app.utb.springrestsecurityapp.security.UserPrincipal;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.ui.response.ErrorMessages;
import com.app.utb.springrestsecurityapp.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            throw new UserServiceException("create user already exists");

        //Simce user can have multiple address
        //we need to loop through the request and
        //generate address for each user as shown below
        for(int i = 0; i < userDto.getAddresses().size();  i++){
            AddressDto addressDto = userDto.getAddresses().get(i);
            addressDto.setUserDetails(userDto);
            addressDto.setAddressId(utils.generateAddressId(30));
            userDto.getAddresses().set(i, addressDto);
        }

        userDto.setUserId(utils.generateUserId(30));

        ModelMapper modelMapper = new ModelMapper();
        //BeanUtils.copyProperties(userDto, userEntity);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
//        userEntity.setUserId("testId");
        userEntity.setEncryptedPassword(passwordEncoder.encode("pass"));
        userEntity.setEmailVerificationToken("testToken");
        UserEntity storedUser = userRepository.save(userEntity);


//        BeanUtils.copyProperties(storedUser, returnedValue);
        UserDto returnedValue = modelMapper.map(storedUser, UserDto.class);
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
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
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

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if( userEntity == null)
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userRepository.delete(userEntity);



    }


    @Override
    public List<UserDto> getUsers(int page, int limit) {
        if(page > 0)
            page -= 1;

        Pageable pageable = PageRequest.of(page, limit);

        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        List<UserEntity> usersList = userEntities.getContent();

        List<UserDto> returnedValue = new ArrayList<>();

        for (UserEntity user: usersList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            returnedValue.add(userDto);
        }
        return returnedValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserEntity storedValue = userRepository.findByEmail(username);


        if(storedValue == null)
            throw new UsernameNotFoundException("User "+ username + " not found");

        return new UserPrincipal(storedValue);


        /*UserDto userDto = new UserDto();
        BeanUtils.copyProperties(storedValue, userDto);
        return new User(storedValue.getEmail(), storedValue.getEncryptedPassword(), new ArrayList<>());*/

    }


}
