package com.example.CloudManagementSystem.repository;

import com.example.CloudManagementSystem.entity.UserProductGstinMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProductGstinMappingRepository extends JpaRepository<UserProductGstinMapping,Integer> {
}
