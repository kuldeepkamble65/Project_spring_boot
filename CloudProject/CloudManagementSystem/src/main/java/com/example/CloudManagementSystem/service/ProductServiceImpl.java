package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.entity.Product;
import com.example.CloudManagementSystem.exception.UserNotFoundException;
import com.example.CloudManagementSystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Product> getProductsByUserId(int userId) {
        List<Product> products = productRepository.findProductsByUserId(userId);
        if(products.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        return products;
    }
}
