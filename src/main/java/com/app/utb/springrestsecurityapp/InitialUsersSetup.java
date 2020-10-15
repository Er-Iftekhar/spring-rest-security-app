package com.app.utb.springrestsecurityapp;


import com.app.utb.springrestsecurityapp.entity.AuthorityEntity;
import com.app.utb.springrestsecurityapp.entity.RoleEntity;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.repositories.AuthorityRepository;
import com.app.utb.springrestsecurityapp.repositories.RoleRepository;
import com.app.utb.springrestsecurityapp.repositories.UserRepository;
import com.app.utb.springrestsecurityapp.utils.Utils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUsersSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final Utils utils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public InitialUsersSetup(AuthorityRepository authorityRepository, RoleRepository roleRepository, Utils utils, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.utils = utils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("From Application ready event");
        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        RoleEntity userRole = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));

        RoleEntity roleAdmin = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        if(roleAdmin !=null){
            UserEntity userEntity = new UserEntity();
            userEntity.setFirstName("admin");
            userEntity.setLastName("admin");
            userEntity.setEmail("admin@gmail.com");
            userEntity.setEmailVerificationStatus(true);
            userEntity.setUserId(utils.generateUserId(30));
            userEntity.setEncryptedPassword(passwordEncoder.encode("pass"));
            userEntity.setRoles(Arrays.asList(roleAdmin));
            userRepository.save(userEntity);
        }

    }


    @Transactional
    private AuthorityEntity createAuthority(String name){

        AuthorityEntity authority = authorityRepository.findByName(name);
        if(authority == null)
        {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }


    @Transactional
    private RoleEntity createRole(
            String name,
            Collection<AuthorityEntity> authorities
    ){
        RoleEntity roleEntity = roleRepository.findByName(name);
        if(roleEntity == null){
            roleEntity = new RoleEntity(name);
            roleEntity.setAuthorities(authorities);
            roleRepository.save(roleEntity);
        }
        return roleEntity;
    }
}
