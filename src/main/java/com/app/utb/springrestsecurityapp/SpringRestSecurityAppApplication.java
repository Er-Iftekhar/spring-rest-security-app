package com.app.utb.springrestsecurityapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringRestSecurityAppApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringRestSecurityAppApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringRestSecurityAppApplication.class, args);
    }
}
