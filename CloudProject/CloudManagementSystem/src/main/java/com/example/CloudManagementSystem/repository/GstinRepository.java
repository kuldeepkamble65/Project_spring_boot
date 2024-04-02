package com.example.CloudManagementSystem.repository;

import com.example.CloudManagementSystem.entity.Gstin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GstinRepository extends JpaRepository<Gstin, Integer> {

    Gstin findByGstinId(int gstinId);
}
