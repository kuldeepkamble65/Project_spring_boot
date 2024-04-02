package com.example.CloudManagementSystem.validation;

import com.example.CloudManagementSystem.entity.User;
import com.example.CloudManagementSystem.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;

public class Uservalidation {


    public static void validateUser(User user) throws ValidationException {
        if (StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getEmail()) ||  StringUtils.isBlank(user.getPassword())) {
            throw new ValidationException("Invalid input. Please provide values for all user properties.");
        }
        validateName(user.getName());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
    }

    private static void validateName(String name) throws ValidationException {
        if (!name.matches("^[A-Za-z]+$")) {
            throw new ValidationException("Invalid name format. Name should contain only letters.");
        }
    }

    private static void validateEmail(String email) throws ValidationException {
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
            throw new ValidationException("Invalid email format.");
        }
    }

    private static void validatePassword(String password) throws ValidationException{
        if (password.length() < 6) {
            throw new ValidationException("Invalid password format. Password should be at least 6 characters long.");
        }
    }
}
