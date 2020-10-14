package com.app.utb.springrestsecurityapp;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InitialUsersSetup {

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("From Application ready event");

    }
}
