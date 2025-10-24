package com.hirehub.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;	
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableScheduling
@SpringBootApplication
public class HireHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(HireHubApplication.class, args);
    }
}
