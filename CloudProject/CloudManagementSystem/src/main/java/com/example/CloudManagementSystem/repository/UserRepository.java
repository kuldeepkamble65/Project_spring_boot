package com.example.CloudManagementSystem.repository;

import com.example.CloudManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    User findByuserId(int userId);

}
