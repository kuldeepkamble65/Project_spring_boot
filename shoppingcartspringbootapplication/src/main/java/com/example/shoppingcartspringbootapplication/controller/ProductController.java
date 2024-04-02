package com.example.shoppingcartspringbootapplication.controller;

import com.example.shoppingcartspringbootapplication.ShoppingcartspringbootapplicationApplication;
import com.example.shoppingcartspringbootapplication.entity.Product;
import com.example.shoppingcartspringbootapplication.service.ProductService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProductController {
    private static final Logger LOGGER = LogManager.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

   @PostMapping
   public ResponseEntity<String> addProduct(@RequestBody Product product) {
//       System.out.println("START >> CLASS >> ProductController >> METHOD >> addProduct >> ");
//       END
//       INTERMEDIATE
//       ERROR
       try {
           productService.addproduct(product);
           LOGGER.info("INTERMEDIATE >> Product added successfully!");
           return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!");
       } catch (Exception e) {
           LOGGER.error("ERROR >> An error occurred while adding the product", e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding the product");
       } finally {
           LOGGER.info("END >>  CLASS >> ProductController >> METHOD >> addProduct >>End of the method");
       }
   }

}
