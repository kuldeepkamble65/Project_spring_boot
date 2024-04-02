package com.example.shoppingcartspringbootapplication.service;

import com.example.shoppingcartspringbootapplication.entity.Product;
import com.example.shoppingcartspringbootapplication.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addproduct(Product product){
       return productRepository.save(product);
    }

}
