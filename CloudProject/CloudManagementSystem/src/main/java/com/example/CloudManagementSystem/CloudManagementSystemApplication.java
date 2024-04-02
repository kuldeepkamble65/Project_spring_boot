package com.example.CloudManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CloudManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudManagementSystemApplication.class, args);
    }

}
