package com.example.CloudManagementSystem.exception;

public class SubscriptionNotFoundException extends RuntimeException{
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
