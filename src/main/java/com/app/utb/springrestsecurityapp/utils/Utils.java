package com.app.utb.springrestsecurityapp.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABETS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public  String generateUserId(int length){

        return generateRandomString(length);
    }

    private String generateRandomString(int length){
        StringBuilder returnedValue=new StringBuilder();
        for (int i=0; i<length; i++){
            returnedValue.append(ALPHABETS.charAt(RANDOM.nextInt(ALPHABETS.length())));
        }
        return  new String(returnedValue);
    }
}
