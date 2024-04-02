package com.example.CloudManagementSystem.repository;

import com.example.CloudManagementSystem.entity.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
        Product findByProductName(String productName);

        @Query("SELECT p FROM Product p JOIN p.userProductGstinMappings u WHERE u.user.userId = :userId")
        List<Product> findProductsByUserId(int userId);
}