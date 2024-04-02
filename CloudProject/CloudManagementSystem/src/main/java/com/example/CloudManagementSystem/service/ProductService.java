package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.entity.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getProductsByUserId(int userId);
}
