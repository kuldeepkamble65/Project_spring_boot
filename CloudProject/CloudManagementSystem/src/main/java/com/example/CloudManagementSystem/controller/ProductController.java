package com.example.CloudManagementSystem.controller;

import com.example.CloudManagementSystem.entity.Product;
import com.example.CloudManagementSystem.exception.UserNotFoundException;
import com.example.CloudManagementSystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable int userId) {
        try {
            List<Product> products = productService.getProductsByUserId(userId);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
