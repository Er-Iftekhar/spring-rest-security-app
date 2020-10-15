package com.app.utb.springrestsecurityapp;


import com.app.utb.springrestsecurityapp.entity.AuthorityEntity;
import com.app.utb.springrestsecurityapp.entity.RoleEntity;
import com.app.utb.springrestsecurityapp.repositories.AuthorityRepository;
import com.app.utb.springrestsecurityapp.repositories.RoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUsersSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    public InitialUsersSetup(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("From Application ready event");
        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
        createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

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
