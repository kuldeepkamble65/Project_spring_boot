package com.example.CloudManagementSystem.service;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncryptionService {


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encryptPassword(String password) {

        return passwordEncoder.encode(password);
    }

    public boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}