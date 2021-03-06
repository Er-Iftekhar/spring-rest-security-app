package com.app.utb.springrestsecurityapp.security;

import com.app.utb.springrestsecurityapp.SpringApplicationContext;
import com.app.utb.springrestsecurityapp.dto.UserDto;
import com.app.utb.springrestsecurityapp.service.UserService;
import com.app.utb.springrestsecurityapp.service.impl.UserServiceImpl;
import com.app.utb.springrestsecurityapp.ui.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
                                                throws AuthenticationException {
        try {
            UserLoginRequestModel reqDetails = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequestModel.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            reqDetails.getEmail(),
                            reqDetails.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        //getting the authenticated user.
        //Since we are implementing UserDetails to our class UserPrincipal
        //So we have get username from UserPrincipal class.
        String username = ((UserPrincipal) authResult.getPrincipal()).getUsername();

        String token = Jwts.builder()//create a JWS object
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        UserService userService=(UserService)SpringApplicationContext.getBean("userServiceImpl");

        UserDto userDto = userService.getUser(username);
        response.addHeader(SecurityConstants.HEADER_STRING,
                SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("UserId ", userDto.getUserId());


    }
}
