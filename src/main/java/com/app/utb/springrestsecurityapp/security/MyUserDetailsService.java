package com.app.utb.springrestsecurityapp.security;

import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserEntity storedValue = userRepository.findByEmail(username);
        if(storedValue == null)
            throw new UsernameNotFoundException("User "+ username + " not found");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(storedValue, userDto);



        return new User(storedValue.getEmail(), storedValue.getEncryptedPassword(), new ArrayList<>());

    }
}
