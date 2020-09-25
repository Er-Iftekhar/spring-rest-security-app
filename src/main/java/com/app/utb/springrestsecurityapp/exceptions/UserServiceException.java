package com.app.utb.springrestsecurityapp.exceptions;

public class UserServiceException extends RuntimeException{

    public UserServiceException(String message) {
        super(message);
    }
}
