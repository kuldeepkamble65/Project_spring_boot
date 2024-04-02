package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.DTO.LoginDto;
import com.example.CloudManagementSystem.DTO.ProductDto;
import com.example.CloudManagementSystem.entity.Subscription;
import com.example.CloudManagementSystem.entity.User;

import java.time.LocalDate;
import java.util.List;


public interface UserService {
    User registerUser(User user);

    void addProductsAndSubscriptions(int userId, List<ProductDto> productDtoList);

    User login(LoginDto loginDto);

    void updateSubStartDate(int subcriptionId, LocalDate newSubStartDate);

    void updateSubEndDate(int subcriptionId, LocalDate newSubEndDate);

    void updateSubscriptionStatuses();
    public List<ProductDto> getProductsByUserId(int userId);
}
