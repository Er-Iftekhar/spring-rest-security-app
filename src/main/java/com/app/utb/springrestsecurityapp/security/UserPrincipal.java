package com.app.utb.springrestsecurityapp.security;

import com.app.utb.springrestsecurityapp.entity.AuthorityEntity;
import com.app.utb.springrestsecurityapp.entity.RoleEntity;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final UserEntity userEntity;
    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        //Get user roles
        Collection<RoleEntity> roles = userEntity.getRoles();
        if( roles == null) return authorities;

        //Since RoleEntity and Authority entities has a ManyToMany relation we need to set Both the collections
        roles.forEach(role->{
            //We need to set this GrantedAuthorities so that it is returned by this predefined method.
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            //First we add what ever this role is having as authorities to a list and then use that list to add those authorities to
            //a list of SimpleGrantedAuthority, in short since spring security does not differentiate between roles and authorities
            // we add both roles and authorities to a list of GrantedAuthority.
            authorityEntities.addAll(role.getAuthorities());
        });

        authorityEntities.forEach(authorityEntity ->
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()))
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEmailVerificationStatus();
    }
}
